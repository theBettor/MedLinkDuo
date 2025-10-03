package com.bettor.medlinkduo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.bettor.medlinkduo.core.ally.a11yClickable
import com.bettor.medlinkduo.di.AppDepsEntryPoint
import com.bettor.medlinkduo.domain.BleDevice
import com.bettor.medlinkduo.domain.ConnectionState
import dagger.hilt.android.EntryPointAccessors

@Composable
fun ScanConnectScreen(
    vm: ConnectViewModel = hiltViewModel(),
    onSynced: () -> Unit
) {
    val devices by vm.devices.collectAsState()
    val state by vm.connection.collectAsState()
    val phase by vm.scanPhase.collectAsState()

    // TTS 가져오기 (EntryPoint 한 줄)
    val ctx = LocalContext.current
    val deps = remember { EntryPointAccessors.fromApplication(ctx, AppDepsEntryPoint::class.java) }
    val tts = deps.tts()

    // 🔔 Haptics
    val haptics = rememberHaptics()
    val ph = rememberPlatformHaptics()  // ← 추가: 플랫폼 파형

    // ✅ 상태별 하프틱
    LaunchedEffect(phase) {
        when (phase) {
            "Scanning" -> haptics.play(HapticEvent.ScanStart)
            "Done"     -> haptics.play(HapticEvent.ScanDone)
        }
    }
    LaunchedEffect(state) {
        when (state) {
            is ConnectionState.Synced       -> haptics.play(HapticEvent.Connected, ph)
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
        val obs = LifecycleEventObserver { _, e ->
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


    // 상태 텍스트(Idle은 노출하지 않음)
    val status = when {
        state is ConnectionState.Synced -> "Connected"
        phase == "Scanning" || state is ConnectionState.Scanning -> "Scanning"
        phase == "Done" -> "Done"
        else -> "Done" // Idle 대신 Done으로 대체
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // 👇 더블탭: 상태 재낭독 / 롱프레스: 간단 도움말
            .a11yReReadGesture(
                onDoubleTap = {
                    val statusSpoken = when (phase) {
                        "Scanning" -> "주변 기기를 찾는 중입니다"
                        "Done"     -> "스캔이 완료되었습니다. 기기를 선택하세요"
                        else -> if (state is ConnectionState.Synced) "연결됨" else "대기 중"
                    }
                    tts.speak(statusSpoken); haptics.play(HapticEvent.ReRead)
                },
                onLongPress = {
                    tts.speak("재스캔은 화면 중앙의 버튼입니다.")
                    haptics.play(HapticEvent.SafeStop)
                }
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 제목(heading) — TalkBack 구조 인식 향상
        Text("기기 선택", modifier = Modifier.semantics { heading() },
            style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(8.dp))

        // 가운데 상태(첫 포커스 진입점)
        Text(
            text = status,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .focusRequester(focusRequester)
                .focusable()
        )
        LaunchedEffect(Unit) { focusRequester.requestFocus() }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { vm.onScan() },
                modifier = Modifier
                    .minTouchTarget()
                    .semantics { role = Role.Button }
            ) { Text("재스캔") }
        }

        Spacer(Modifier.height(20.dp))

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
private fun DeviceRow(d: BleDevice, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .a11yClickable(
                desc = "장치 ${d.name ?: d.id}, 신호 ${d.rssi}",
                label = "연결"
            ) { onClick() }
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(d.name ?: d.id, style = MaterialTheme.typography.titleMedium)
            Text("RSSI ${d.rssi}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}