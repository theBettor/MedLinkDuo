package com.bettor.medlinkduo.data.tts

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.bettor.medlinkduo.core.common.AudioFocusManager
import com.bettor.medlinkduo.domain.TtsController
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import java.util.UUID
import java.util.concurrent.CancellationException
import java.util.concurrent.atomic.AtomicInteger
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

        // 중단을 구분하기 위한 내부 예외
        private object TtsStopped : CancellationException("TTS stopped") {
            private fun readResolve(): Any = TtsStopped
        }

        // 오디오 포커스 관리(중복 요청 안전)
        private val audioFocus = AudioFocusManager(ctx)
        private val focusHolders = AtomicInteger(0)

        private fun requestFocus() {
            if (focusHolders.getAndIncrement() == 0) audioFocus.request()
        }

        private fun abandonFocus() {
            if (focusHolders.decrementAndGet() <= 0) {
                focusHolders.set(0)
                audioFocus.abandon()
            }
        }

        private lateinit var tts: TextToSpeech

        init {
            val engine =
                TextToSpeech(ctx) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        tts.language = Locale.getDefault()
                        ready.complete(Unit)
                    } else {
                        ready.completeExceptionally(IllegalStateException("TTS init failed: $status"))
                    }
                }

            engine.setOnUtteranceProgressListener(
                object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) = Unit

                    override fun onDone(utteranceId: String?) {
                        utteranceId?.let { waiters.remove(it)?.invoke(Result.success(Unit)) }
                        abandonFocus() // ✅ 발화 종료 시 포커스 반납
                    }

                    override fun onError(utteranceId: String?) {
                        utteranceId?.let { waiters.remove(it)?.invoke(Result.failure(RuntimeException("TTS error"))) }
                        abandonFocus() // ✅ 에러 시도 반납
                    }

                    @Deprecated("Deprecated in Java", ReplaceWith("onError(utteranceId)"))
                    override fun onError(
                        utteranceId: String?,
                        errorCode: Int,
                    ) = onError(utteranceId)

                    override fun onStop(
                        utteranceId: String?,
                        interrupted: Boolean,
                    ) {
                        // ⬇️ '중단'은 정상 흐름으로 전달(나중에 swallow)
                        utteranceId?.let { waiters.remove(it)?.invoke(Result.failure(TtsStopped)) }
                        abandonFocus() // ✅ stop 시도 반납
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

            requestFocus() // ✅ 대기형 발화도 시작 전에 포커스 획득
            try {
                // 결과를 받아 '중단'만 정상 종료로 치환
                val result: Result<Unit> =
                    suspendCancellableCoroutine { cont ->
                        waiters[id] = { r -> if (cont.isActive) cont.resume(r) }

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

                result.fold(
                    onSuccess = { /* ok */ },
                    onFailure = { e ->
                        // 'stop()'으로 끊긴 경우는 정상 종료로 간주(throw 하지 않음)
                        if (e !is TtsStopped) throw e
                    },
                )
            } finally {
                // onDone/onStop에서도 반납하지만, 예외/조기종료에 대비해 이중보장(Ref-count라 중복 안전)
                abandonFocus()
            }
        }

        override fun stop() {
            runCatching { tts.stop() }
            abandonFocus()
        }

        override fun shutdown() {
            runCatching { tts.shutdown() }
            // 혹시 남아있을 ref-count 정리
            while (focusHolders.getAndSet(0) > 0) { /* drain */ }
            audioFocus.abandon()
        }

        // --- internal ---

        private fun speakNow(text: String) {
            requestFocus() // ✅ 즉시 발화도 포커스 획득
            val id = UUID.randomUUID().toString()
            val params =
                Bundle().apply {
                    putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, id)
                }
            val code = tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, id)
            if (code != TextToSpeech.SUCCESS) {
                abandonFocus() // 실패 시 즉시 반납
            }
        }
    }
