package com.bettor.medlinkduo.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.repeatOnLifecycle
import com.bettor.medlinkduo.R.string.measurement_btn_end
import com.bettor.medlinkduo.R.string.measurement_btn_pause
import com.bettor.medlinkduo.R.string.measurement_btn_start
import com.bettor.medlinkduo.R.string.measurement_title
import com.bettor.medlinkduo.R.string.measurement_waiting
import com.bettor.medlinkduo.R.string.nav_go_to_scan
import com.bettor.medlinkduo.core.common.Phase
import com.bettor.medlinkduo.core.di.AppDepsEntryPoint
import com.bettor.medlinkduo.core.ui.Command
import com.bettor.medlinkduo.core.ui.HapticEvent
import com.bettor.medlinkduo.core.ui.VoiceButton
import com.bettor.medlinkduo.core.ui.a11yGestures
import com.bettor.medlinkduo.core.ui.minTouchTarget
import com.bettor.medlinkduo.core.ui.play
import com.bettor.medlinkduo.core.ui.rememberActionGuard
import com.bettor.medlinkduo.core.ui.rememberHaptics
import com.bettor.medlinkduo.core.ui.rememberPlatformHaptics
import com.bettor.medlinkduo.core.ui.rememberVoiceCommandLauncher
import com.bettor.medlinkduo.ui.viewmodel.SessionViewModel
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

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
    val sensory = deps.sensory()
    val speakNumeric = deps.speakNumeric()

    val last by vm.last.collectAsState()
    val ui by vm.ui.collectAsState()

    val scope = rememberCoroutineScope()
    val haptics = rememberHaptics()
    val ph = rememberPlatformHaptics() // ← 추가: 플랫폼 파형

    // 첫 포커스(중앙 값)
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    // 음성 명령: 더블탭 → 음성 / 롱프레스 → 긴급 중단
    val launchVoice =
        rememberVoiceCommandLauncher(
            allowed = setOf(Command.ReMeasure, Command.Pause, Command.End, Command.GoScan),
            onCommand = { cmd ->
                when (cmd) {
                    Command.ReMeasure -> vm.remeasure()
                    Command.Pause -> {
                        vm.pause()
                        sensory.error()
                        haptics.play(HapticEvent.SafeStop, ph)
                    }
                    Command.End -> {
                        vm.end()
                        onShowFeedback()
                    }
                    Command.GoScan -> {
                        vm.end()
                        onGoToScan()
                    }
                    else -> Unit
                }
            },
        )

    // 화면 벗어나면 즉시 정지 (ON_PAUSE)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs =
            LifecycleEventObserver { _, e ->
                if (e == Lifecycle.Event.ON_PAUSE) vm.pause()
            }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    // RESUMED 상태에서만 새 측정값 숫자 낭독(측정 중일 때)
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            combine(vm.last, vm.ui) { m, u -> m to u.phase }.collect { (m, phase) ->
                if (phase == Phase.Measuring && m != null) {
                    // suspend 안전 구간
                    speakNumeric(m) // 예: "120"
                }
            }
        }
    }

    // MeasurementScreen 내부 어딘가(Composable 최상단 근처)
    BackHandler(enabled = true) {
        // 뒤로가기 무시 + 선택: 경고음/안내
        sensory.error()
        tts.speak("현재 화면에서는 뒤로가기가 지원되지 않습니다.")
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                // 👇 더블탭: 마지막 값 재낭독 / 롱프레스: 어디서든 ‘긴급 중단’
                .a11yGestures(
                    onDoubleTap = { launchVoice() }, // ✅ 공통: 더블탭=음성
                    onLongPress = { // ✅ 안전: 롱프레스=긴급중단
                        vm.pause()
                        sensory.error()
                        haptics.play(HapticEvent.SafeStop, ph)
                        tts.speak("측정을 중단했습니다.")
                    },
                )
                .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 제목(heading)
        Text(
            stringResource(measurement_title),
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
                text = last?.value ?: stringResource(measurement_waiting),
                style = MaterialTheme.typography.displaySmall,
                modifier =
                    Modifier
                        .focusRequester(focusRequester)
                        .focusable(),
            )
        }

        Spacer(Modifier.height(20.dp))

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
                        sensory.tick() // 시작 알림(짧게)
                        haptics.play(HapticEvent.Connected, ph)
                        vm.remeasure()
                    }
                },
                enabled = !guard.acting && !ui.busy && ui.phase != Phase.Measuring,
                modifier =
                    Modifier
                        .minTouchTarget()
                        .semantics { role = Role.Button },
            ) { Text(stringResource(measurement_btn_start)) }

            // 중단
            OutlinedButton(
                onClick = {
                    guard.launch {
                        vm.pause()
                        sensory.vibrate(60) // 손에 확 느껴지게
                        haptics.play(HapticEvent.SafeStop, ph)
                        tts.speak("측정을 중단했습니다.")
                    }
                },
                enabled = !guard.acting && !ui.busy && ui.phase == Phase.Measuring,
                modifier =
                    Modifier
                        .minTouchTarget()
                        .semantics { role = Role.Button },
            ) { Text(stringResource(measurement_btn_pause)) }

            // 측정 종료 → Feedback
            OutlinedButton(
                onClick = {
                    guard.launch {
                        vm.end()
                        sensory.success() // 완료 찰칵
                        haptics.play(HapticEvent.ScanDone, ph)
                        tts.speakAndWait("측정 결과를 보여드립니다.")
                        onShowFeedback()
                    }
                },
                enabled = !guard.acting && !ui.busy && ui.phase != Phase.Idle,
                modifier =
                    Modifier
                        .minTouchTarget()
                        .semantics { role = Role.Button },
            ) { Text(stringResource(measurement_btn_end)) }

            VoiceButton(
                allowed = setOf(Command.ReMeasure, Command.Pause, Command.End, Command.GoScan, Command.RepeatResult),
                onCommand = { cmd ->
                    when (cmd) {
                        Command.ReMeasure ->
                            scope.launch {
                                tts.speakAndWait("측정을 시작합니다.")
                                vm.remeasure()
                            }

                        Command.Pause -> {
                            vm.pause()
                            tts.speak("측정을 중단했습니다.")
                        }

                        Command.End ->
                            scope.launch {
                                vm.end()
                                tts.speakAndWait("측정 결과를 보여드립니다.")
                                onShowFeedback()
                            }

                        Command.GoScan ->
                            scope.launch {
                                vm.end()
                                tts.speakAndWait("기기 선택 화면으로 돌아갑니다.")
                                onGoToScan()
                            }

                        Command.RepeatResult -> last?.let { scope.launch { speakNumeric(it) } }
                        else -> Unit
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
                modifier =
                    Modifier
                        .minTouchTarget()
                        .semantics { role = Role.Button },
            ) { Text(stringResource(nav_go_to_scan)) }
        }
    }
}
