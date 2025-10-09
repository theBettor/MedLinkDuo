package com.bettor.medlinkduo.ui.screens

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import com.bettor.medlinkduo.core.common.Phase
import com.bettor.medlinkduo.core.di.AppDepsEntryPoint
import com.bettor.medlinkduo.core.ui.HapticEvent
import com.bettor.medlinkduo.core.ui.a11yReReadGesture
import com.bettor.medlinkduo.core.ui.minTouchTarget
import com.bettor.medlinkduo.core.ui.play
import com.bettor.medlinkduo.core.ui.rememberActionGuard
import com.bettor.medlinkduo.core.ui.rememberHaptics
import com.bettor.medlinkduo.core.ui.rememberPlatformHaptics
import com.bettor.medlinkduo.ui.viewmodel.SessionViewModel
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeasurementScreen(
    vm: SessionViewModel = hiltViewModel(),
    onGoToScan: () -> Unit, // 스캔 화면으로 이동
    onShowFeedback: () -> Unit, // ⬅️ 추가: 피드백 화면으로 이동
) {
    val ctx = LocalContext.current
    val deps = remember { EntryPointAccessors.fromApplication(ctx, AppDepsEntryPoint::class.java) }
    val tts = deps.tts()
    val speakNumeric = deps.speakNumeric()

    val last by vm.last.collectAsState()
    val scope = rememberCoroutineScope()

    val haptics = rememberHaptics()
    val ph = rememberPlatformHaptics() // ← 추가: 플랫폼 파형

    // 첫 포커스(중앙 값)
    val focusRequester = remember { FocusRequester() }

    // 화면 벗어나면 즉시 정지
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs =
            LifecycleEventObserver { _, e ->
                if (e == Lifecycle.Event.ON_PAUSE) vm.pause()
            }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose {
            vm.pause()
            lifecycleOwner.lifecycle.removeObserver(obs)
        }
    }

    Column(
        modifier =
            Modifier
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
                    },
                )
                .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 제목(heading)
        Text(
            "측정",
            modifier = Modifier.semantics { heading() },
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(Modifier.height(8.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .height(220.dp)
                .border(2.dp, MaterialTheme.colorScheme.onBackground),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = last?.value ?: "측정 대기 중…",
                style = MaterialTheme.typography.displaySmall,
                modifier =
                    Modifier
                        .focusRequester(focusRequester)
                        .focusable(),
            )
        }
        LaunchedEffect(Unit) { focusRequester.requestFocus() }

        Spacer(Modifier.height(20.dp))

        // 상단 state 구독 + 가드 플래그 추가
        val ui by vm.ui.collectAsState()
        val guard = rememberActionGuard(scope) // ← 한 줄로 가드 준비

        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // 측정
            Button(
                onClick = {
                    guard.launch {
                        tts.speakAndWait("측정을 시작합니다.")
                        haptics.play(HapticEvent.Connected, ph)
                        vm.remeasure()
                    }
                },
                enabled = !guard.acting && !ui.busy && ui.phase != Phase.Measuring,
                modifier = Modifier.minTouchTarget().semantics { role = Role.Button },
            ) { Text("측정") }

            // 중단
            OutlinedButton(
                onClick = {
                    guard.launch {
                        vm.pause()
                        haptics.play(HapticEvent.SafeStop, ph)
                        tts.speak("측정을 중단했습니다.")
                    }
                },
                enabled = !guard.acting && !ui.busy && ui.phase == Phase.Measuring,
                modifier = Modifier.minTouchTarget().semantics { role = Role.Button },
            ) { Text("중단") }

            // 측정 종료 → Feedback
            OutlinedButton(
                onClick = {
                    guard.launch {
                        vm.end()
                        haptics.play(HapticEvent.ScanDone, ph)
                        tts.speakAndWait("측정 결과를 보여드립니다.")
                        onShowFeedback()
                    }
                },
                enabled = !guard.acting && !ui.busy && ui.phase != Phase.Idle,
                modifier = Modifier.minTouchTarget().semantics { role = Role.Button },
            ) { Text("측정 종료") }

            // 음성명령
            VoiceCommandButton(
                onRepeat = { last?.let { m -> scope.launch { speakNumeric(m) } } },
                onReMeasure = {
                    guard.launch {
                        tts.speakAndWait("측정을 시작합니다.")
                        vm.remeasure()
                    }
                },
                onPause = {
                    guard.launch {
                        vm.pause()
                        tts.speak("측정을 중단했습니다.")
                    }
                },
                onEnd = {
                    guard.launch {
                        vm.end()
                        tts.speakAndWait("측정 결과를 보여드립니다.")
                        onShowFeedback()
                    }
                },
                onGoScan = {
                    guard.launch {
                        vm.end()
                        tts.speakAndWait("기기 선택 화면으로 돌아갑니다.")
                        onGoToScan()
                    }
                },
            )

            // 기기 선택 화면으로
            OutlinedButton(
                onClick = {
                    guard.launch {
                        vm.end()
                        tts.speakAndWait("기기 선택 화면으로 돌아갑니다.")
                        onGoToScan()
                    }
                },
                enabled = !guard.acting,
                modifier = Modifier.minTouchTarget().semantics { role = Role.Button },
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
    onGoScan: () -> Unit,
) {
    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { res ->
            val text =
                res.data
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

    val intent =
        remember {
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
                )
                putExtra(
                    RecognizerIntent.EXTRA_PROMPT,
                    "명령을 말하세요: 측정, 중단, 측정 종료, 기기 선택, 다시 읽어줘",
                )
            }
        }

    OutlinedButton(onClick = { launcher.launch(intent) }) {
        Text("음성명령")
    }
}
