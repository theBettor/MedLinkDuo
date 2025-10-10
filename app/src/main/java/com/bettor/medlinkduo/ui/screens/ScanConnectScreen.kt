package com.bettor.medlinkduo.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.bettor.medlinkduo.R
import com.bettor.medlinkduo.R.string.scan_btn_rescan
import com.bettor.medlinkduo.R.string.scan_device_connect_label
import com.bettor.medlinkduo.R.string.scan_title
import com.bettor.medlinkduo.core.di.AppDepsEntryPoint
import com.bettor.medlinkduo.core.ui.Command
import com.bettor.medlinkduo.core.ui.HapticEvent
import com.bettor.medlinkduo.core.ui.VoiceButton
import com.bettor.medlinkduo.core.ui.a11yClickable
import com.bettor.medlinkduo.core.ui.a11yReReadGesture
import com.bettor.medlinkduo.core.ui.minTouchTarget
import com.bettor.medlinkduo.core.ui.play
import com.bettor.medlinkduo.core.ui.rememberHaptics
import com.bettor.medlinkduo.core.ui.rememberPlatformHaptics
import com.bettor.medlinkduo.domain.BleDevice
import com.bettor.medlinkduo.domain.ConnectionState
import com.bettor.medlinkduo.ui.viewmodel.ConnectViewModel
import com.bettor.medlinkduo.ui.viewmodel.SpeechTag
import dagger.hilt.android.EntryPointAccessors

@Composable
fun ScanConnectScreen(
    vm: ConnectViewModel = hiltViewModel(),
    onSynced: () -> Unit,
) {
    val devices by vm.devices.collectAsState()
    val state by vm.connection.collectAsState()
    val phase by vm.scanPhase.collectAsState()

    // TTS 가져오기 (EntryPoint 한 줄)
    val ctx = LocalContext.current
    val deps = remember { EntryPointAccessors.fromApplication(ctx, AppDepsEntryPoint::class.java) }
    val tts = deps.tts()
    val sensory = deps.sensory() // ← 이거 한 줄만 추가

    // 🔔 Haptics
    val haptics = rememberHaptics()
    val ph = rememberPlatformHaptics() // ← 추가: 플랫폼 파형

    // ✅ 상태별 하프틱
    LaunchedEffect(phase) {
        when (phase) {
            "Scanning" -> haptics.play(HapticEvent.ScanStart)
            "Done" -> haptics.play(HapticEvent.ScanDone)
        }
    }
    LaunchedEffect(state) {
        when (state) {
            is ConnectionState.Synced -> haptics.play(HapticEvent.Connected, ph)
            is ConnectionState.Disconnected -> haptics.play(HapticEvent.Error, ph)
            else -> {}
        }
    }
    // ✅ 첫 포커스(상태 텍스트)
    val focusRequester = remember { FocusRequester() }

    // 화면 보일 때 1회 자동 스캔
    LaunchedEffect(Unit) { vm.ensureScan() }

    // 앱 복귀(ON_RESUME) 때도 자동 스캔 보장
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs =
            LifecycleEventObserver { _, e ->
                if (e == Lifecycle.Event.ON_RESUME) vm.ensureScan()
            }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    // 🔊 VM의 말하기 큐를 한 곳에서만 소비(순차 재생)
    LaunchedEffect(vm) {
        vm.speech.collect { s ->
            if (s.await) tts.speakAndWait(s.text) else tts.speak(s.text)
            when (s.tag) {
                SpeechTag.NavigateToMeas -> onSynced()
                null -> Unit
            }
        }
    }

    // 상태 전이마다 1회
    LaunchedEffect(phase) {
        when (phase) {
            "Scanning" -> sensory.tick() // 스캔 시작
            "Done" -> sensory.success() // 스캔 완료
        }
    }
    LaunchedEffect(state) {
        when (state) {
            is ConnectionState.Synced -> sensory.success() // 연결 성공
            is ConnectionState.Disconnected -> sensory.error() // 끊김/오류
            else -> Unit
        }
    }

    // 상태 텍스트(Idle은 노출하지 않음)
    val status =
        when {
            state is ConnectionState.Synced -> "Connected"
            phase == "Scanning" || state is ConnectionState.Scanning -> "Scanning"
            phase == "Done" -> "Done"
            else -> "Done" // Idle 대신 Done으로 대체
        }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                // 👇 더블탭: 상태 재낭독 / 롱프레스: 간단 도움말
                .a11yReReadGesture(
                    onDoubleTap = {
                        val statusSpoken =
                            when (phase) {
                                "Scanning" -> "주변 기기를 찾는 중입니다"
                                "Done" -> "스캔이 완료되었습니다. 기기를 선택하세요"
                                else -> if (state is ConnectionState.Synced) "연결됨" else "대기 중"
                            }
                        tts.speak(statusSpoken)
                        haptics.play(HapticEvent.ReRead)
                    },
                    onLongPress = {
                        tts.speak("재스캔은 화면 중앙의 버튼입니다.")
                        haptics.play(HapticEvent.SafeStop)
                    },
                )
                .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 제목(heading) — TalkBack 구조 인식 향상
        Text(
            stringResource(scan_title),
            modifier = Modifier.semantics { heading() },
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(Modifier.height(8.dp))

        // 가운데 상태(첫 포커스 진입점)
        Text(
            text = status,
            style = MaterialTheme.typography.headlineMedium,
            modifier =
                Modifier
                    .focusRequester(focusRequester)
                    .focusable(),
        )
        LaunchedEffect(Unit) { focusRequester.requestFocus() }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = {
                    sensory.tick()
                    vm.onScan()
                },
                modifier =
                    Modifier
                        .minTouchTarget()
                        .semantics { role = Role.Button },
            ) { stringResource(scan_btn_rescan) }
        }

        Spacer(Modifier.height(20.dp))

        // ScanConnectScreen.kt - 재스캔 버튼 아래 등 원하는 위치
        VoiceButton(
            allowed = setOf(Command.Rescan, Command.RepeatResult, Command.GoScan),
            onCommand = { cmd ->
                when (cmd) {
                    Command.Rescan -> vm.onScan()
                    Command.RepeatResult -> // 현재 상태 말하기
                        tts.speak(
                            when (status) {
                                "Scanning" -> "주변 기기를 찾는 중입니다"
                                "Done" -> "스캔이 완료되었습니다. 기기를 선택하세요"
                                else -> status
                            },
                        )

                    Command.GoScan -> { // 현재 화면이므로 무시 or 도움말
                    }

                    else -> Unit
                }
            },
        )

        // 장치 리스트(최대 2개)
        LazyColumn(Modifier.weight(1f)) {
            items(devices.take(2), key = { it.id }) { d ->
                DeviceRow(d) { vm.onConnect(d) }
                Divider()
            }
        }
    }
}

@Composable
private fun DeviceRow(
    d: BleDevice,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .a11yClickable(
                desc =
                    stringResource(
                        R.string.scan_device_desc,
                        d.name ?: d.id, // %1$s
                        d.rssi, // %2$d
                    ),
                label = stringResource(scan_device_connect_label),
            ) { onClick() }
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(d.name ?: d.id, style = MaterialTheme.typography.titleMedium)
            Text(
                text = stringResource(R.string.scan_device_rssi_dbm, d.rssi),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
