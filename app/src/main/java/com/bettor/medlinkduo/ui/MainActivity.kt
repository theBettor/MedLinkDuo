package com.bettor.medlinkduo.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bettor.medlinkduo.core.common.PermissionMgr
import com.bettor.medlinkduo.ui.screens.FeedbackScreen
import com.bettor.medlinkduo.ui.screens.MeasurementScreen
import com.bettor.medlinkduo.ui.screens.ScanConnectScreen
import com.bettor.medlinkduo.ui.viewmodel.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onResume() {
        super.onResume()
        // 권한이 꺼져 있으면 메인 진입 차단 → PermissionActivity로 위임
        if (!PermissionMgr.allGranted(this)) {
            startActivity(
                Intent(this, PermissionActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                },
            )
            finish()
            return
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⚠️ 여기서는 권한을 다시 체크/finish 하지 않습니다.
        // PermissionActivity가 이미 권한을 보장하고 Main을 태스크 루트로 띄웁니다.

        setContent {
            val nav = rememberNavController()

            NavHost(nav, startDestination = "scan") {
                composable("scan") { ScanConnectScreen(onSynced = { nav.navigate("meas") }) }

                composable("meas") {
                    val svm: SessionViewModel = hiltViewModel()
                    MeasurementScreen(
                        vm = svm,
                        onGoToScan = {
                            nav.popBackStack(route = "scan", inclusive = false)
                        },
                        onShowFeedback = { nav.navigate("fb") },
                    )
                }

                composable("fb") {
                    // ‘meas’와 같은 SessionViewModel 인스턴스를 공유하고 싶다면
                    // 필요 시 backStackEntry를 parent로 잡아 hiltViewModel(parent) 사용
                    val svm: SessionViewModel = hiltViewModel()
                    FeedbackScreen(
                        vm = svm,
                        onGoToScan = {
                            nav.popBackStack() // 피드백 닫기
                            nav.popBackStack(route = "scan", inclusive = false) // 스캔으로
                        },
                    )
                }
            }
        }
    }
}
