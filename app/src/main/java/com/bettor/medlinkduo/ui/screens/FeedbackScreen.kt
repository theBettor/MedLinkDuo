package com.bettor.medlinkduo.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.bettor.medlinkduo.R.string.feedback_last_value
import com.bettor.medlinkduo.R.string.feedback_no_value
import com.bettor.medlinkduo.R.string.feedback_title
import com.bettor.medlinkduo.R.string.nav_go_to_scan
import com.bettor.medlinkduo.core.di.AppDepsEntryPoint
import com.bettor.medlinkduo.core.ui.Command
import com.bettor.medlinkduo.core.ui.HapticEvent
import com.bettor.medlinkduo.core.ui.a11yGestures
import com.bettor.medlinkduo.core.ui.minTouchTarget
import com.bettor.medlinkduo.core.ui.play
import com.bettor.medlinkduo.core.ui.rememberHaptics
import com.bettor.medlinkduo.core.ui.rememberPlatformHaptics
import com.bettor.medlinkduo.core.ui.rememberVoiceCommandLauncher
import com.bettor.medlinkduo.ui.viewmodel.SessionViewModel
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun FeedbackScreen(
    vm: SessionViewModel, // ⬅️ 같은 인스턴스를 주입받음
    onGoToScan: () -> Unit,
) {
    val last by vm.last.collectAsState()

    val ctx = LocalContext.current
    val deps =
        remember {
            EntryPointAccessors.fromApplication(ctx, AppDepsEntryPoint::class.java)
        }
    val speakNumeric = deps.speakNumeric()
    val sensory = deps.sensory()
    val tts = deps.tts()

    val scope = rememberCoroutineScope()
    val haptics = rememberHaptics()
    val ph = rememberPlatformHaptics()

    // 포커스 + 진입 하프틱을 하나의 이펙트로
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        haptics.play(HapticEvent.ScanDone)
        focusRequester.requestFocus()
    }

    // 더블탭 = 음성 명령(여기는 GoScan만 허용), 롱프레스 = 닫고 스캔으로
    val launchVoice =
        rememberVoiceCommandLauncher(
            allowed = setOf(Command.GoScan),
            onCommand = { onGoToScan() },
        )

    // 화면이 RESUMED일 때만 요약의 마지막 값 숫자 낭독
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            vm.summary.collectLatest { s ->
                s?.last?.let { speakNumeric(it) }
            }
        }
    }

    BackHandler(enabled = true) {
        sensory.error()
        tts.speak("피드백 화면에서는 뒤로가기가 지원되지 않습니다. 화면을 길게 눌러 스캔 화면으로 돌아갈 수 있습니다.")
    }

    // 🔽 아래 UI는 “기존 디자인”에 맞춰 바꿔 넣어도 됨
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                // 더블탭: 마지막 값 재낭독 / 롱프레스: 피드백 닫기
                .a11yGestures(
                    onDoubleTap = { launchVoice() }, // ✅ 공통: 더블탭=음성(스캔으로)
                    onLongPress = { // ✅ 보조: 롱프레스=닫고 스캔으로
                        haptics.play(HapticEvent.SafeStop, ph)
                        onGoToScan()
                    },
                )
                .padding(20.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        // 제목: heading 지정 + 첫 포커스 진입점
        Text(
            stringResource(feedback_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier =
                Modifier
                    .semantics { heading() }
                    .focusRequester(focusRequester)
                    .focusable(),
        )

        Spacer(Modifier.height(12.dp))

        // 결과 카드
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .border(2.dp, MaterialTheme.colorScheme.onBackground)
                    .padding(16.dp),
        ) {
            val line =
                vm.summary.collectAsState().value?.last?.let {
                    stringResource(feedback_last_value, it.value)
                } ?: stringResource(feedback_no_value)

            Text(
                text = line,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // 기기 선택 화면으로
            OutlinedButton(
                onClick = {
                    scope.launch {
                        tts.speakAndWait("기기 선택 화면으로 돌아갑니다.")
                        onGoToScan()
                    }
                },
                modifier = Modifier.minTouchTarget().semantics { role = Role.Button },
            ) { Text(stringResource(nav_go_to_scan)) }
        }
    }
}
