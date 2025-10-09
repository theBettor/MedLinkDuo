package com.bettor.medlinkduo.ui.screens

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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bettor.medlinkduo.core.di.AppDepsEntryPoint
import com.bettor.medlinkduo.core.ui.HapticEvent
import com.bettor.medlinkduo.core.ui.a11yReReadGesture
import com.bettor.medlinkduo.core.ui.minTouchTarget
import com.bettor.medlinkduo.core.ui.play
import com.bettor.medlinkduo.core.ui.rememberHaptics
import com.bettor.medlinkduo.core.ui.rememberPlatformHaptics
import com.bettor.medlinkduo.ui.viewmodel.SessionViewModel
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch

@Composable
fun FeedbackScreen(
    vm: SessionViewModel, // ⬅️ 같은 인스턴스를 주입받음
    onGoToScan: () -> Unit,
) {
    val summary by vm.summary.collectAsState()
    val last by vm.last.collectAsState()

    val ctx = LocalContext.current
    val deps =
        remember {
            EntryPointAccessors.fromApplication(ctx, AppDepsEntryPoint::class.java)
        }
    val speakNumeric = deps.speakNumeric()

    val tts = deps.tts()
    val scope = rememberCoroutineScope()
    val haptics = rememberHaptics()
    val ph = rememberPlatformHaptics() // ← 추가: 플랫폼 파형

    // 화면 진입 시 짧은 확인 하프틱(중복 음성은 피함)
    LaunchedEffect(Unit) { haptics.play(HapticEvent.ScanDone) }

    // 첫 포커스: 제목
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    // 진입 시 숫자만 낭독(있다면)
    LaunchedEffect(summary?.last?.ts) {
        summary?.last?.let { speakNumeric(it) }
    }

    // 🔽 아래 UI는 “기존 디자인”에 맞춰 바꿔 넣어도 됨
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                // 더블탭: 마지막 값 재낭독 / 롱프레스: 피드백 닫기
                .a11yReReadGesture(
                    onDoubleTap = {
                        last?.let {
                            scope.launch { speakNumeric(it) }
                            haptics.play(HapticEvent.ReRead, ph) // 👈 넘겨도 OK
                        }
                    },
                    onLongPress = {
                        scope.launch {
                            haptics.play(HapticEvent.SafeStop, ph) // 👈 긴급 종료 느낌
                            tts.speakAndWait("피드백 화면을 닫습니다.")
                            onGoToScan()
                        }
                    },
                )
                .padding(20.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        // 제목: heading 지정 + 첫 포커스 진입점
        Text(
            "세션 요약",
            style = MaterialTheme.typography.headlineMedium,
            modifier =
                Modifier
                    .semantics { heading() }
                    .focusRequester(focusRequester)
                    .focusable(),
        )

        Spacer(Modifier.height(12.dp))

        // 결과 카드(필요 시 스타일 조정)
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .border(2.dp, MaterialTheme.colorScheme.onBackground)
                    .padding(16.dp),
        ) {
            val line = summary?.last?.let { "마지막 측정값: ${it.value}" } ?: "측정값이 없습니다."
            Text(line, textAlign = TextAlign.Start, style = MaterialTheme.typography.titleLarge)
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
            ) { Text("기기 선택 화면으로") }
        }
    }
}
