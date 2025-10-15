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
            var completed by rememberSaveable { mutableStateOf(false) }          // 메인 전환 1회 보장
            var inFlight by rememberSaveable { mutableStateOf(false) }           // 시스템 대화상자 요청 중
            var permanentlyDenied by rememberSaveable { mutableStateOf(false) }  // OS가 대화상자 차단 상태
            var denyCount by rememberSaveable { mutableStateOf(0) }              // [허용 안함]/취소 누적 횟수(결과단에서만 +1)

            // 현재 시점에 ‘미허용’ 권한만 계산
            fun currentMissing(): Array<String> =
                PermissionMgr.required()
                    .filter { p -> ContextCompat.checkSelfPermission(ctx, p) != PERMISSION_GRANTED }
                    .toTypedArray()

            // OS 차단 판단: 두 가지 모두 만족할 때만 영구차단으로 간주
            fun computePermanentlyDenied(): Boolean {
                val osBlocks = PermissionMgr.isPermanentlyDenied(this@PermissionActivity)
                return (denyCount >= 2) && osBlocks
            }

            // 1) 권한 결과 런처 (대화상자 닫힐 때마다 콜백)
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { _ ->
                inFlight = false
                val stillMissing = currentMissing().isNotEmpty()

                if (!stillMissing) {
                    // ✅ [허용] → 모든 권한 OK
                    if (!completed) {
                        sensory.success()
                        tts.speak("권한이 허용되었습니다.")
                        completed = true
                        goMain()
                    }
                } else {
                    // ❌ (1) 허용 안함  (2) 뒤로가기   (3) 바깥 터치
                    denyCount += 1
                    sensory.error()

                    permanentlyDenied = computePermanentlyDenied()

                    scope.launch {
                        if (permanentlyDenied) {
                            // 🚫 [허용 안함]을 여러 번 → OS가 더 이상 다이얼로그를 안 띄움
                            tts.speakAndWait(
                                "권한이 여러 차례 거부되어 허용 창을 더 이상 열 수 없습니다. " +
                                        "주변의 도움을 받아 앱 설정에서 권한을 켜 주세요."
                            )
                        } else {
                            // ↩️ 바깥 터치/뒤로가기/1회 거부 등 → 재요청 가능
                            tts.speakAndWait(
                                "권한이 허용되지 않았습니다. 주변의 도움을 받아 권한을 켜 주세요. " +
                                        "화면을 한 번 탭하면 허용 창이 열립니다."
                            )
                        }
                    }
                }
            }

            // 2) 실제로 시스템 권한 대화상자를 띄우는 트리거 (빈 화면 탭/최초 진입 모두 이 경로)
            fun triggerRequest() {
                // OS가 차단한 상태거나, 이미 진행 중/완료면 중복 호출 방지
                if (completed || inFlight || permanentlyDenied) return

                val toAsk = currentMissing()
                if (toAsk.isNotEmpty()) {
                    inFlight = true
                    sensory.tick()
                    launcher.launch(toAsk)   // 🔔 여기서 시스템 대화상자 표시
                } else if (!completed) {
                    completed = true
                    goMain()
                }
            }

            // 3) 단일 탭 핸들러
            val handleTap: () -> Unit = {
                if (permanentlyDenied) {
                    // OS가 차단한 상태 → 다이얼로그 불가 → 안내만 반복
                    tts.speak(
                        "권한이 여러 차례 거부되어 허용 창을 더 이상 열 수 없습니다. " +
                                "주변의 도움을 받아 앱 설정에서 권한을 켜 주세요."
                    )
                } else {
                    // 소프트 거부/취소 케이스 → 언제든 다시 띄움
                    triggerRequest()
                }
            }

            // 4) 최초 진입: 짧게 안내 후 즉시 요청
            LaunchedEffect(Unit) {
                if (!PermissionMgr.allGranted(ctx)) {
                    tts.speak("권한 요청 창이 열렸습니다. 허용 버튼을 눌러 주세요.")
                    triggerRequest()
                }
            }

            // 5) UI — 단일 탭만 사용 (TalkBack 더블탭도 onClick으로 들어옴)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .semantics {
                        contentDescription =
                            if (permanentlyDenied)
                                "권한이 여러 차례 거부되어 허용 창을 더 이상 열 수 없습니다. 주변의 도움을 받아 앱 설정에서 권한을 켜 주세요."
                            else
                                "화면을 한 번 탭하면 권한을 다시 요청합니다."
                        onClick(label = if (permanentlyDenied) "안내" else "권한 다시 요청") {
                            handleTap(); true
                        }
                    }
                    .clickable { handleTap() },
                contentAlignment = Alignment.Center
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
            }
        )
        finish()
    }
}
