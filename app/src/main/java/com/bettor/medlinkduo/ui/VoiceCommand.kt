package com.bettor.medlinkduo.ui

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.bettor.medlinkduo.core.permission.PermissionMgr
import dagger.hilt.android.EntryPointAccessors
import com.bettor.medlinkduo.di.AppDepsEntryPoint

@Composable
fun VoiceButton(
    onCommand: (Command) -> Unit
) {
    val ctx = LocalContext.current
    val activity = ctx as Activity
    val deps = remember {
        EntryPointAccessors.fromApplication(ctx, AppDepsEntryPoint::class.java)
    }
    val tts = deps.tts()

    // 마이크 권한 요청 런처 (필요 시)
    val audioPermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) tts.speak("말씀해 주세요.")
        else tts.speak("마이크 권한이 필요합니다.")
    }

    // 음성 인식 런처
    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        val data = res.data
        val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        val text = results?.firstOrNull()?.lowercase() ?: ""
        val cmd = parseCommand(text)
        if (cmd != null) {
            onCommand(cmd)
        } else {
            tts.speak("명령을 이해하지 못했습니다. 예: 재스캔, 다시 읽어줘, 피드백.")
        }
    }

    Button(onClick = {
        // RECORD_AUDIO 권한 확인/요청
        val mic = android.Manifest.permission.RECORD_AUDIO
        if (activity.checkSelfPermission(mic) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            audioPermLauncher.launch(mic)
            return@Button
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "명령을 말하세요")
        }
        speechLauncher.launch(intent)
    }) {
        Text("음성 명령")
    }
}

sealed class Command {
    data object Rescan : Command()
    data object RepeatResult : Command()
    data object OpenFeedback : Command()
}

private fun parseCommand(text: String): Command? = when {
    "재스캔" in text || "다시 스캔" in text || "스캔" in text -> Command.Rescan
    "다시 읽어줘" in text || "읽어 줘" in text || "읽어줘" in text -> Command.RepeatResult
    "피드백" in text || "의견" in text -> Command.OpenFeedback
    else -> null
}