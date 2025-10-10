package com.bettor.medlinkduo.ui.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.bettor.medlinkduo.core.common.PermissionMgr
import com.bettor.medlinkduo.core.di.AppDepsEntryPoint
import dagger.hilt.android.EntryPointAccessors

@Composable
fun PermissionGate(
    onAllGranted: () -> Unit,
) {
    val ctx = LocalContext.current
    val activity = ctx as Activity
    val lifecycleOwner = LocalLifecycleOwner.current

    val deps = remember {
        // AppDepsEntryPoint 패키지 경로에 맞춰 import 필요
        EntryPointAccessors.fromApplication(ctx, AppDepsEntryPoint::class.java)
    }
    val tts = deps.tts()
    val onboarding = deps.onboardingStore()
    val sensory = deps.sensory() // 👈 추가: 삑/진동

    val required = remember { PermissionMgr.required() }
    var launchedOnce by rememberSaveable { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val all = result.values.all { it }
        if (all) {
            sensory.success() // 👈 모두 허용
            tts.speak("권한이 허용되었습니다. 스캔 화면으로 이동합니다.")
            onAllGranted()
        } else {
            sensory.error()   // 👈 거부/취소
            launchedOnce = true // 재요청/설정 분기
        }
    }

    // 처음 진입: 온보딩 멘트 → 권한 요청(열리기 직전에 tick)
    LaunchedEffect(Unit) {
        if (PermissionMgr.allGranted(ctx)) {
            // 이미 허용 상태라면 바로 성공 처리
            sensory.success()
            onAllGranted()
        } else {
            if (onboarding.shouldSpeak()) {
                tts.speak(
                    "환영합니다. 이 앱은 화면 없이도 사용할 수 있도록 설계되었습니다. " +
                            "지금 시스템 권한 창이 열리면, 허용 버튼을 두 번 탭해 주세요."
                )
                onboarding.markSpoken()
            } else {
                tts.speak("시스템 권한 창이 열립니다. 허용 버튼을 두 번 탭해 주세요.")
            }
            sensory.tick()     // 👈 권한 다이얼로그 뜨기 직전 짧은 피드백
            launcher.launch(required)
        }
    }

    // 설정 다녀와서 복귀 시 자동 확인(+성공 피드백)
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, e ->
            if (e == Lifecycle.Event.ON_RESUME) {
                if (PermissionMgr.allGranted(ctx)) {
                    sensory.success() // 👈 설정에서 허용하고 돌아온 경우
                    tts.speak("권한이 허용되었습니다. 스캔 화면으로 이동합니다.")
                    onAllGranted()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    // 재요청/설정 분기 판단
    val mustGoSettings =
        launchedOnce &&
                !PermissionMgr.shouldShowAnyRationale(activity) &&
                !PermissionMgr.allGranted(ctx)

    // 빈 화면 전체를 큰 버튼처럼: 두 번 탭(스크린리더)로 재요청/설정 열기
    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                contentDescription =
                    if (mustGoSettings) {
                        "권한이 차단되었습니다. 화면을 두 번 탭하면 설정을 엽니다."
                    } else {
                        "화면을 두 번 탭하면 권한을 다시 요청합니다."
                    }
                onClick(label = if (mustGoSettings) "설정 열기" else "권한 다시 요청") {
                    if (mustGoSettings) {
                        sensory.tick() // 👈 액션 피드백
                        tts.speak("설정을 엽니다. 권한에서 블루투스를 허용해 주세요.")
                        activity.startActivity(PermissionMgr.appSettingsIntent(activity))
                    } else {
                        sensory.tick() // 👈 재요청 전 피드백
                        tts.speak("권한을 다시 요청합니다. 허용 버튼을 두 번 탭해 주세요.")
                        launcher.launch(required)
                    }
                    true
                }
            }
            .clickable {
                if (mustGoSettings) {
                    sensory.tick()
                    tts.speak("설정을 엽니다. 권한에서 블루투스를 허용해 주세요.")
                    activity.startActivity(PermissionMgr.appSettingsIntent(activity))
                } else {
                    sensory.tick()
                    tts.speak("권한을 다시 요청합니다. 허용 버튼을 두 번 탭해 주세요.")
                    launcher.launch(required)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        /* 시각 요소 없음(빈 화면) */
    }
}
