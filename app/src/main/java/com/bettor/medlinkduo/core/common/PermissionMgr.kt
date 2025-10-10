package com.bettor.medlinkduo.core.common

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat

/**
 * BLE + 알림 권한 유틸 (런타임 요청 대상만 반환).
 * Foreground Service 권한(FGS/CONNECTED_DEVICE)은 '선언'만 필요, 런타임 요청 대상 아님.
 */
object PermissionMgr {
    /** SDK별 필요한 런타임 권한 배열을 반환 (확장함수 사용 X) */
    @SuppressLint("InlinedApi")
    fun required(): Array<String> {
        val list = java.util.ArrayList<String>(6)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            list.add(Manifest.permission.BLUETOOTH_SCAN)
            list.add(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            list.add(Manifest.permission.BLUETOOTH)
            list.add(Manifest.permission.BLUETOOTH_ADMIN)
            list.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            list.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        // ⚠️ Kotlin 확장 대신, 자바 컬렉션의 toArray 사용
        return list.toArray(arrayOf<String>())
    }

    /** 아직 허용되지 않은 권한들만 반환 */
    private fun missing(context: Context): Array<String> {
        val need = required()
        val miss = java.util.ArrayList<String>(need.size)
        for (p in need) {
            val granted = ContextCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_GRANTED
            if (!granted) miss.add(p)
        }
        return miss.toArray(arrayOf<String>())
    }

    /** 전부 허용되었는지 (확장함수 isEmpty() 대신 size 비교) */
    fun allGranted(context: Context): Boolean {
        val m = missing(context)
        return m.isEmpty()
    }

    /** 라쇼날(다시 묻기 설명) 필요한 권한이 하나라도 있는지 */
    fun shouldShowAnyRationale(activity: Activity): Boolean {
        val need = required()
        for (p in need) {
            if (activity.shouldShowRequestPermissionRationale(p)) return true
        }
        return false
    }

    /** 앱 설정 화면으로 이동 Intent (권한 영구거부 안내용) */
    fun appSettingsIntent(context: Context): Intent {
        return Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null),
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
