package com.bettor.medlinkduo.core.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.bettor.medlinkduo.core.di.AppDepsEntryPoint
import dagger.hilt.android.EntryPointAccessors
import java.util.Locale

// 화면 공통 명령 세트(필요하면 더 추가)
sealed class Command {
    data object Rescan : Command()

    data object RepeatResult : Command()

    data object ReMeasure : Command()

    data object Pause : Command()

    data object End : Command()

    data object GoScan : Command()

    data object OpenFeedback : Command()
}

private fun parseCommand(text: String): Command? =
    when {
        listOf("재스캔", "다시 스캔", "스캔", "scan").any { it in text } -> Command.Rescan
        listOf("다시 읽어줘", "다시", "읽어 줘", "repeat", "read").any { it in text } -> Command.RepeatResult
        listOf("측정", "시작", "remeasure", "measure").any { it in text } -> Command.ReMeasure
        listOf("중단", "멈춰", "pause", "stop").any { it in text } -> Command.Pause
        listOf("종료", "측정 종료", "end", "finish").any { it in text } -> Command.End
        listOf("기기 선택", "스캔 화면", "스캔으로", "go scan").any { it in text } -> Command.GoScan
        listOf("피드백", "의견").any { it in text } -> Command.OpenFeedback
        else -> null
    }

/**
 * 공용 음성 명령 버튼.
 * - 마이크 권한 요청 → 프롬프트 TTS → 인식 → 명령 콜백
 * - SensoryFeedback(비프/진동)도 함께 사용
 */
@Composable
fun VoiceButton(
    onCommand: (Command) -> Unit,
    allowed: Set<Command>? = null,
    prompt: String = "명령을 말하세요: 재스캔, 다시 읽어줘, 측정, 중단, 측정 종료, 기기 선택",
) {
    val ctx = LocalContext.current
    val activity = ctx as Activity

    val deps =
        remember {
            EntryPointAccessors.fromApplication(ctx, AppDepsEntryPoint::class.java)
        }
    val tts = deps.tts()
    val sensory = deps.sensory()

    // RECORD_AUDIO 권한 요청 런처
    val requestMicPermission =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { granted ->
            if (granted) {
                sensory.tick()
                tts.speak("말씀해 주세요.")
            } else {
                sensory.error()
                tts.speak("마이크 권한이 필요합니다.")
            }
        }

    // 음성 인식 런처
    val speechLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { res ->
            val text =
                res.data
                    ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?.firstOrNull()
                    ?.lowercase(Locale.getDefault())
                    ?: ""

            val cmd = parseCommand(text)
            if (cmd == null) {
                sensory.error()
                tts.speak("명령을 이해하지 못했습니다. 예: 재스캔, 다시 읽어줘, 측정.")
                return@rememberLauncherForActivityResult
            }

            if (allowed != null && cmd !in allowed) {
                sensory.error()
                tts.speak("이 화면에서는 지원하지 않는 명령입니다.")
                return@rememberLauncherForActivityResult
            }

            sensory.tick()
            onCommand(cmd)
        }

    OutlinedButton(
        onClick = {
            val mic = Manifest.permission.RECORD_AUDIO
            val granted = ContextCompat.checkSelfPermission(activity, mic) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                requestMicPermission.launch(mic)
                return@OutlinedButton
            }
            val intent =
                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "명령을 말하세요")
                }
            sensory.tick()
            tts.speak("말씀해 주세요.")
            speechLauncher.launch(intent)
        },
    ) {
        Text("음성 명령")
    }
}
