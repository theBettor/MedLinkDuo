package com.bettor.medlinkduo.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import com.bettor.medlinkduo.core.common.PermissionMgr
import com.bettor.medlinkduo.core.di.AppDepsEntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors

@AndroidEntryPoint
class PermissionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val ctx = LocalContext.current
            val activity = ctx as Activity
            val tts =
                remember {
                    EntryPointAccessors.fromApplication(ctx, AppDepsEntryPoint::class.java).tts()
                }
            var launchedOnce by remember { mutableStateOf(false) }

            // 권한 요청 런처
            val launcher =
                rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions(),
                ) { result ->
                    val all = result.values.all { it }
                    if (all) {
                        goMain()
                    } else {
                        launchedOnce = true
                    }
                }

            // 최초 진입: OK면 바로 Main, 아니면 자동 요청
            LaunchedEffect(Unit) {
                if (PermissionMgr.allGranted(ctx)) {
                    goMain()
                } else {
                    tts.speak("시스템 권한 창이 열렸습니다. 허용을 위해 화면을 두 번 탭해 주세요.")
                    launcher.launch(PermissionMgr.required())
                }
            }

            // 다시 묻지 않기라 창을 못 띄울 때 → 설정 이동
            val mustGoSettings =
                launchedOnce &&
                    !PermissionMgr.shouldShowAnyRationale(activity) &&
                    !PermissionMgr.allGranted(ctx)

            // 설정 갔다가 복귀하면 onResume에서 재검사

            // 빈 화면 전체를 큰 버튼처럼 사용(두 번 탭 제스처 대응)
            Box(
                modifier =
                    Modifier
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
                                    tts.speak("설정을 엽니다. 권한에서 블루투스를 허용해 주세요.")
                                    startActivity(PermissionMgr.appSettingsIntent(activity))
                                } else {
                                    tts.speak("권한을 다시 요청합니다. 허용을 두 번 탭해 주세요.")
                                    launcher.launch(PermissionMgr.required())
                                }
                                true
                            }
                        }
                        .clickable {
                            if (mustGoSettings) {
                                tts.speak("설정을 엽니다. 권한에서 블루투스를 허용해 주세요.")
                                startActivity(PermissionMgr.appSettingsIntent(activity))
                            } else {
                                tts.speak("권한을 다시 요청합니다. 허용을 두 번 탭해 주세요.")
                                launcher.launch(PermissionMgr.required())
                            }
                        },
                contentAlignment = Alignment.Center,
            ) { /* 시각요소 없음 */ }
        }
    }

    override fun onResume() {
        super.onResume()
        // 설정에서 돌아온 경우 포함: 권한 OK면 곧바로 Main
        if (PermissionMgr.allGranted(this)) goMain()
    }

    private fun goMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
