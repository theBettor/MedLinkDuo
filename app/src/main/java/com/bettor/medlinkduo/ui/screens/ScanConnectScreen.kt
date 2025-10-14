package com.bettor.medlinkduo.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.bettor.medlinkduo.R
import com.bettor.medlinkduo.R.string.scan_device_connect_label
import com.bettor.medlinkduo.R.string.scan_title
import com.bettor.medlinkduo.core.di.AppDepsEntryPoint
import com.bettor.medlinkduo.core.ui.Command
import com.bettor.medlinkduo.core.ui.HapticEvent
import com.bettor.medlinkduo.core.ui.VoiceButton
import com.bettor.medlinkduo.core.ui.a11yClickable
import com.bettor.medlinkduo.core.ui.a11yGestures
import com.bettor.medlinkduo.core.ui.play
import com.bettor.medlinkduo.core.ui.rememberHaptics
import com.bettor.medlinkduo.core.ui.rememberVoiceCommandLauncher
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

    // DI
    val ctx = LocalContext.current
    val deps = remember { EntryPointAccessors.fromApplication(ctx, AppDepsEntryPoint::class.java) }
    val tts = deps.tts()
    val sensory = deps.sensory() // ← 이거 한 줄만 추가

    // Haptics
    val haptics = rememberHaptics()

    // 1) status — 계산 값으로
    val status =
        remember(phase, state) {
            when {
                state is ConnectionState.Synced -> "Connected"
                phase == "Scanning" || state is ConnectionState.Scanning -> "Scanning"
                phase == "Done" -> "Done"
                else -> "Done"
            }
        }

    // 2) RESUMED 때만 보장(필요하면 앞에서 1회 호출 추가)
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner, vm) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            vm.ensureScan()
        }
    }

    // 말하기 큐는 한 곳에서만 소비
    LaunchedEffect(vm) {
        vm.speech.collect { s ->
            if (s.await) tts.speakAndWait(s.text) else tts.speak(s.text)
            if (s.tag == SpeechTag.NavigateToMeas) onSynced()
        }
    }

    // phase/state 변화에 따른 하프틱/사운드도 한 곳에서
    LaunchedEffect(phase, state) {
        when (phase) {
            "Scanning" -> {
                haptics.play(HapticEvent.ScanStart)
                sensory.tick()
            }
            "Done" -> {
                haptics.play(HapticEvent.ScanDone)
                sensory.success()
            }
        }
        when (state) {
            is ConnectionState.Synced -> {
                haptics.play(HapticEvent.Connected)
                sensory.success()
            }
            is ConnectionState.Disconnected -> {
                haptics.play(HapticEvent.Error)
                sensory.error()
            }
            else -> Unit
        }
    }

    // ✅ 스캔 결과만 1회 낭독 (진행 멘트 제거)
    var announcedDone by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(phase, devices.size) {
        if (phase == "Scanning") {
            announcedDone = false
        } else if (phase == "Done" && !announcedDone) {
            announcedDone = true
            if (devices.isNotEmpty()) {
                tts.speak("${devices.size}대의 기기를 찾았습니다. 목록에서 선택해 주세요.")
            } else {
                tts.speak("주변 기기를 찾지 못했습니다. 화면을 길게 눌러 재탐색하세요.")
            }
        }
    }

    // 음성 명령: 더블탭 → 음성 시작 / 롱프레스 → 재탐색
    val launchVoice =
        rememberVoiceCommandLauncher(
            allowed = setOf(Command.Rescan),
            onCommand = { cmd ->
                if (cmd == Command.Rescan) {
                    sensory.tick()
                    vm.onScan()
                }
            },
        )

    // 3) 포커스 요청은 한 번만
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    // 4) 리스트 공간/상태 보장
    val listState = rememberLazyListState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                // 👇 더블탭: 상태 재낭독 / 롱프레스: 간단 도움말
                .a11yGestures(
                    onDoubleTap = { launchVoice() },
                    onLongPress = {
                        sensory.tick()
                        haptics.play(HapticEvent.ScanStart)
                        vm.onScan()
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

        VoiceButton(
            allowed = setOf(Command.Rescan, Command.RepeatResult, Command.GoScan),
            onCommand = { cmd ->
                when (cmd) {
                    Command.Rescan -> vm.onScan()

                    // ✅ 완료 상태일 때만 결과 낭독
                    Command.RepeatResult -> {
                        if (phase == "Done") {
                            if (devices.isNotEmpty()) {
                                tts.speak("${devices.size}대의 기기를 찾았습니다. 목록에서 선택해 주세요.")
                            } else {
                                tts.speak("주변 기기를 찾지 못했습니다. 화면을 길게 눌러 재탐색하세요.")
                            }
                        }
                    }

                    else -> Unit
                }
            },
        )

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
            state = listState,
        ) {
            items(devices, key = { it.id }) { d ->
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
