package com.bettor.medlinkduo.ui

import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.core.content.ContextCompat
import com.bettor.medlinkduo.core.common.PermissionMgr
import com.bettor.medlinkduo.core.di.AppDepsEntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PermissionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 이미 허용이면 바로 메인
        if (PermissionMgr.allGranted(this)) {
            goMain()
            return
        }

        setContent {
            val ctx = LocalContext.current
            val deps = remember { EntryPointAccessors.fromApplication(ctx, AppDepsEntryPoint::class.java) }
            val tts = deps.tts()
            val sensory = deps.sensory()
            val scope = rememberCoroutineScope()

            // 진행 상태
            var completed by rememberSaveable { mutableStateOf(false) } // 메인 전환 1회 보장
            var inFlight by rememberSaveable { mutableStateOf(false) } // 시스템 대화상자 요청 중
            var permanentlyDenied by rememberSaveable { mutableStateOf(false) } // OS가 더 이상 대화상자 안 띄움(‘다시 묻지 않기’)

            // 현재 시점에 ‘미허용’ 권한만 계산
            fun currentMissing(): Array<String> =
                PermissionMgr.required()
                    .filter { p -> ContextCompat.checkSelfPermission(ctx, p) != PERMISSION_GRANTED }
                    .toTypedArray()

            // 영구 거부 여부
            fun isPermanentlyDenied(): Boolean = PermissionMgr.isPermanentlyDenied(this@PermissionActivity)

            // 결과 런처
            val launcher =
                rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions(),
                ) { _ ->
                    inFlight = false
                    val stillMissing = currentMissing().isNotEmpty()
                    if (!stillMissing) {
                        if (!completed) {
                            sensory.success()
                            tts.speak("권한이 허용되었습니다.")
                            completed = true
                            goMain()
                        }
                    } else {
                        // 거부됨
                        sensory.error()
                        permanentlyDenied = isPermanentlyDenied()

                        if (permanentlyDenied) {
                            // OS가 더 이상 창을 안 띄움 → 재요청 막고 안내만
                            scope.launch {
                                tts.speakAndWait(
                                    "권한이 여러 차례 거부되어 허용 창을 더 이상 열 수 없습니다. " +
                                        "주변의 도움을 받아 앱 설정에서 권한을 켜주세요..",
                                )
                            }
                        } else {
                            // 소프트 거부 → 단일 탭으로 언제든 재요청 가능
                            scope.launch {
                                tts.speakAndWait(
                                    "권한이 허용되지 않았습니다. 주변의 도움을 받아 권한을 켜 주세요. " +
                                        "화면을 한번 탭하면 허용 창이 열립니다.",
                                )
                            }
                        }
                    }
                }

            // 시스템 권한 대화상자 실행
            fun launchPermissionDialog() {
                if (completed || inFlight || permanentlyDenied) return

                val toAsk = currentMissing()

                // 권한을 물어볼 항목이 있을 때만 런처 호출
                if (toAsk.isNotEmpty()) {
                    inFlight = true
                    sensory.tick()
                    launcher.launch(toAsk)
                    return
                }

                // 더 물어볼 게 없으면 바로 메인으로
                if (!completed) {
                    completed = true
                    goMain()
                }
            }

            // 최초 진입: 짧게 안내하고 바로 시스템 대화상자 오픈
            LaunchedEffect(Unit) {
                if (!PermissionMgr.allGranted(ctx)) {
                    tts.speak("권한 요청 창이 곧 열립니다. 확인 버튼을 눌러 주세요.")
                    launchPermissionDialog()
                }
            }

            // UI: 단일 탭만 사용
            val canRetry = !completed && !inFlight && !permanentlyDenied
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .semantics {
                            contentDescription =
                                if (permanentlyDenied) {
                                    "권한이 여러 차례 거부되어 허용 창을 더 이상 열 수 없습니다. " +
                                        "주변의 도움을 받아 앱 설정에서 권한을 켜주세요.."
                                } else {
                                    "화면을 한번 탭하면 권한을 다시 요청합니다."
                                }
                            onClick(label = if (permanentlyDenied) "안내" else "권한 다시 요청") {
                                if (permanentlyDenied) {
                                    // 재요청 불가 상태: 멘트만 반복
                                    tts.speak(
                                        "권한이 여러 차례 거부되어 허용 창을 더 이상 열 수 없습니다. " +
                                            "주변의 도움을 받아 앱 설정에서 권한을 켜주세요..",
                                    )
                                } else {
                                    launchPermissionDialog()
                                }
                                true
                            }
                        }
                        .clickable {
                            if (permanentlyDenied) {
                                tts.speak(
                                    "권한이 여러 차례 거부되어 허용 창을 더 이상 열 수 없습니다. " +
                                        "주변의 도움을 받아 앱 설정에서 권한을 켜주세요..",
                                )
                            } else {
                                launchPermissionDialog()
                            }
                        },
                contentAlignment = Alignment.Center,
            ) { /* 시각 요소 없음(빈 화면) */ }
        }
    }

    override fun onResume() {
        super.onResume()
        // 대화상자에서 돌아와 이미 허용됐다면 바로 메인으로
        if (PermissionMgr.allGranted(this)) goMain()
    }

    private fun goMain() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            },
        )
        finish()
    }
}
