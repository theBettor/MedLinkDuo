package com.bettor.medlinkduo.data.tts

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.bettor.medlinkduo.domain.TtsController
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class AndroidTtsController
    @Inject
    constructor(
        @ApplicationContext private val ctx: Context,
    ) : TtsController {
        private val ready = CompletableDeferred<Unit>()
        private val waiters = mutableMapOf<String, (Result<Unit>) -> Unit>()

        // 콜백에서 참조할 수 있도록 lateinit
        private lateinit var tts: TextToSpeech

        init {
            // 1) 엔진 생성
            val engine =
                TextToSpeech(ctx) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        // 3) 콜백은 비동기 → 여기서는 이미 tts에 대입이 끝난 상태
                        tts.language = Locale.getDefault()
                        ready.complete(Unit)
                    } else {
                        ready.completeExceptionally(IllegalStateException("TTS init failed: $status"))
                    }
                }

            // 2) 리스너 부착 후 필드에 대입
            engine.setOnUtteranceProgressListener(
                object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) = Unit

                    override fun onDone(utteranceId: String?) {
                        utteranceId?.let { waiters.remove(it)?.invoke(Result.success(Unit)) }
                    }

                    override fun onError(utteranceId: String?) {
                        utteranceId?.let { waiters.remove(it)?.invoke(Result.failure(RuntimeException("TTS error"))) }
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(
                        utteranceId: String?,
                        errorCode: Int,
                    ) = onError(utteranceId)

                    override fun onStop(
                        utteranceId: String?,
                        interrupted: Boolean,
                    ) {
                        utteranceId?.let { waiters.remove(it)?.invoke(Result.failure(RuntimeException("TTS stopped"))) }
                    }
                },
            )

            tts = engine
        }

        /** 큐를 비우고 즉시 말함. 엔진 준비 전이면 준비 후 자동 발화. */
        override fun speak(text: String) {
            if (text.isBlank()) return
            if (ready.isCompleted) {
                speakNow(text)
            } else {
                // 엔진 초기화 끝난 뒤 한 번만 실행
                ready.invokeOnCompletion { speakNow(text) }
            }
        }

        /** 발화를 시작하고 **끝날 때까지 suspend**. */
        override suspend fun speakAndWait(text: String) {
            if (text.isBlank()) return
            ready.await()

            val id = UUID.randomUUID().toString()
            val params =
                Bundle().apply {
                    putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, id)
                }

            val result =
                suspendCancellableCoroutine<Result<Unit>> { cont ->
                    // 완료/에러 콜백 등록
                    waiters[id] = { r -> if (cont.isActive) cont.resume(r) }

                    // 취소 시 정리
                    cont.invokeOnCancellation {
                        waiters.remove(id)
                        runCatching { tts.stop() }
                    }

                    val code = tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, id)
                    if (code != TextToSpeech.SUCCESS) {
                        waiters.remove(id)
                        cont.resumeWithException(IllegalStateException("speak failed: $code"))
                    }
                }

            result.getOrThrow()
        }

        override fun stop() {
            runCatching { tts.stop() }
        }

        override fun shutdown() {
            runCatching { tts.shutdown() }
        }

        // --- internal ---

        private fun speakNow(text: String) {
            val id = UUID.randomUUID().toString()
            val params =
                Bundle().apply {
                    putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, id)
                }
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, id)
        }
    }
