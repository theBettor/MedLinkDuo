package com.bettor.medlinkduo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bettor.medlinkduo.core.permission.PermissionMgr
import com.bettor.medlinkduo.ui.ConnectViewModel
import com.bettor.medlinkduo.ui.FeedbackScreen
import com.bettor.medlinkduo.ui.FeedbackViewModel
import com.bettor.medlinkduo.ui.MeasurementScreen
import com.bettor.medlinkduo.ui.PermissionGate
import com.bettor.medlinkduo.ui.ScanConnectScreen
import com.bettor.medlinkduo.ui.SessionViewModel
import com.bettor.medlinkduo.ui.theme.MedLinkDuoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onResume() {
        super.onResume()
        if (!PermissionMgr.allGranted(this)) {
            // 권한이 꺼져 있으면 메인 진입 자체를 차단
            startActivity(Intent(this, PermissionActivity::class.java))
            finish()
            return
        }
    }
    @SuppressLint("UnrememberedGetBackStackEntry")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val nav = rememberNavController()

            NavHost(nav, startDestination = "perm") {
                composable("perm") {
                    PermissionGate(
                        onAllGranted = {
                            nav.navigate("scan") { popUpTo("perm") { inclusive = true } }
                        },
                        onCancel = { finish() }
                    )
                }
                composable("scan") {
                    ScanConnectScreen(onSynced = { nav.navigate("meas") })
                }

                composable("meas") {
                    val svm: SessionViewModel = hiltViewModel()
                    MeasurementScreen(
                        vm = svm,
                        onGoToScan = {
                            // 네가 만든 goToScan() 헬퍼가 있으면 사용
                            // nav.goToScan()
                            nav.popBackStack(route = "scan", inclusive = false)
                        },
                        onShowFeedback = { nav.navigate("fb") }   // ⬅️ 전체화면 Feedback으로 이동
                    )
                }

                composable("fb") { backStackEntry ->
                    // ‘meas’의 BackStackEntry를 parent로 잡아 같은 VM 인스턴스를 공유
                    val parentEntry = remember(backStackEntry) { nav.getBackStackEntry("meas") }
                    val svm: SessionViewModel = hiltViewModel(parentEntry)

                    FeedbackScreen(
                        vm = svm,
                        onGoToScan = {
                            nav.popBackStack()                             // Feedback 닫기
                            nav.popBackStack(route = "scan", inclusive = false) // 스캔으로
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MedLinkDuoTheme {
        Greeting("Android")
    }
}

fun NavController.goToScan() {
    val popped = try { popBackStack(route = "scan", inclusive = false) } catch (_: Throwable) { false }
    if (!popped) {
        navigate("scan") {
            popUpTo(graph.startDestinationId) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
}