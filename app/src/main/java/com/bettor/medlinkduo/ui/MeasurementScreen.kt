package com.bettor.medlinkduo.ui

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.bettor.medlinkduo.di.AppDepsEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeasurementScreen(
    vm: SessionViewModel = hiltViewModel(),
    onGoToScan: () -> Unit,   // 스캔 화면으로 이동
    onShowFeedback: () -> Unit,   // ⬅️ 추가: 피드백 화면으로 이동
) {
    val ctx = LocalContext.current
    val deps = remember { EntryPointAccessors.fromApplication(ctx, AppDepsEntryPoint::class.java) }
    val tts = deps.tts()
    val speakNumeric = deps.speakNumeric()

    val last by vm.last.collectAsState()
    val scope = rememberCoroutineScope()

    val haptics = rememberHaptics()
    val ph = rememberPlatformHaptics()  // ← 추가: 플랫폼 파형

    // 첫 포커스(중앙 값)
    val focusRequester = remember { FocusRequester() }


    // 화면 벗어나면 즉시 정지
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, e ->
            if (e == Lifecycle.Event.ON_PAUSE) vm.pause()
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose {
            vm.pause()
            lifecycleOwner.lifecycle.removeObserver(obs)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // 👇 더블탭: 마지막 값 재낭독 / 롱프레스: 어디서든 ‘긴급 중단’
            .a11yReReadGesture(
                onDoubleTap = {
                    last?.let { scope.launch { speakNumeric(it) } }
                    haptics.play(HapticEvent.ReRead)
                },
                onLongPress = {
                    vm.pause()
                    haptics.play(HapticEvent.SafeStop)
                    tts.speak("측정을 중단했습니다.")
                }
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 제목(heading)
        Text(
            "측정", modifier = Modifier.semantics { heading() },
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .height(220.dp)
                .border(2.dp, MaterialTheme.colorScheme.onBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = last?.value ?: "측정 대기 중…",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .focusable()
            )
        }
        LaunchedEffect(Unit) { focusRequester.requestFocus() }

        Spacer(Modifier.height(20.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3, // 화면 폭에 맞춰 2~3으로 조절 가능
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement   = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        tts.speakAndWait("측정을 시작합니다.")
                        haptics.play(HapticEvent.Connected, ph)   // 👈 성공 패턴(짧게-간격-짧게)
                        vm.remeasure()
                    }
                },
                modifier = Modifier
                    .minTouchTarget()
                    .semantics { role = Role.Button }
            ) { Text("측정") }

            OutlinedButton(
                onClick = {
                    vm.pause()
                    haptics.play(HapticEvent.SafeStop, ph)        // 👈 넘겨도 동작 동일
                    tts.speak("측정을 중단했습니다.")
                },
                modifier = Modifier
                    .minTouchTarget()
                    .semantics { role = Role.Button }
            ) { Text("중단") }

            OutlinedButton(
                onClick = {
                    vm.end()
                    scope.launch {
                        haptics.play(HapticEvent.ScanDone, ph)    // (선택) 종료 직전 짧은 확인
                        tts.speakAndWait("측정 결과를 보여드립니다.")
                        onShowFeedback()
                    }
                },
                modifier = Modifier
                    .minTouchTarget()
                    .semantics { role = Role.Button }
            ) { Text("측정 종료") }

            // 음성명령 버튼
            VoiceCommandButton(
                onRepeat = {
                    last?.let { m -> scope.launch { speakNumeric(m) } }
                },
                onReMeasure = {
                    scope.launch {
                        // tts.stop()  // (선택)
                        tts.speakAndWait("측정을 시작합니다.")
                        vm.remeasure()
                    }
                },
                onPause = {
                    vm.pause()
                    tts.speak("측정을 중단했습니다.")
                },
                onEnd = {
                    vm.end()
                    scope.launch {
                        tts.speakAndWait("측정 결과를 보여드립니다.")
                        onShowFeedback()
                    }
                },
                onGoScan = {
                    vm.end()
                    scope.launch {
                        tts.speakAndWait("기기 선택 화면으로 돌아갑니다.")
                        onGoToScan()
                    }
                }
            )
            OutlinedButton(
                onClick = {
                    vm.end()
                    scope.launch {
                        tts.speakAndWait("기기 선택 화면으로 돌아갑니다.")
                        onGoToScan()
                    }
                },
                modifier = Modifier
                    .minTouchTarget()
                    .semantics { role = Role.Button }
            ) { Text("기기 선택 화면으로") }
        }
    }
}


@Composable
fun VoiceCommandButton(
    onRepeat: () -> Unit,
    onReMeasure: () -> Unit,
    onPause: () -> Unit,
    onEnd: () -> Unit,
    onGoScan: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        val text = res.data
            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            ?.firstOrNull()
            ?.lowercase(Locale.getDefault())
            ?: return@rememberLauncherForActivityResult

        when {
            listOf("다시", "다시 읽어줘", "repeat", "read").any { it in text } -> onRepeat()
            listOf("측정", "측정", "remeasure").any { it in text } -> onReMeasure()
            listOf("중단", "pause", "멈춰").any { it in text } -> onPause()
            listOf("종료", "측정 종료", "end").any { it in text } -> onEnd()
            listOf("스캔", "기기 선택", "scan").any { it in text } -> onGoScan()
        }
    }

    val intent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "명령을 말하세요: 측정, 중단, 측정 종료, 기기 선택, 다시 읽어줘"
            )
        }
    }

    OutlinedButton(onClick = { launcher.launch(intent) }) {
        Text("음성명령")
    }
}