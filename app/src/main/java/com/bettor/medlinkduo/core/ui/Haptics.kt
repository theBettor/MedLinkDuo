package com.bettor.medlinkduo.core.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback

enum class HapticEvent { ScanStart, ScanDone, Connected, Error, SafeStop, ReRead }

@Composable
fun rememberHaptics(): HapticFeedback = LocalHapticFeedback.current

@Composable
fun rememberPlatformHaptics(): PlatformHaptics {
    // 1) CompositionLocal을 먼저 읽고
    val ctx = LocalContext.current.applicationContext
    // 2) 그 값을 key로 사용해 객체를 생성/캐시
    return remember(ctx) { PlatformHaptics(ctx) }
}

/** 시스템 진동 API(파형) */
class PlatformHaptics(ctx: Context) {
    private val vibrator: Vibrator? =
        if (Build.VERSION.SDK_INT >= 31) {
            (ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }

    /** 연결 성공: 짧게-간격-짧게 */
    fun success() = vibratePattern(longArrayOf(0, 35, 50, 35))

    /** 에러: 짧게 3회 */
    fun error() = vibratePattern(longArrayOf(0, 25, 40, 25, 40, 25))

    private fun vibratePattern(pattern: LongArray) {
        vibrator ?: return
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, -1)
        }
    }
}

/** Compose haptics + (선택) 플랫폼 파형 */
fun HapticFeedback.play(
    event: HapticEvent,
    platform: PlatformHaptics? = null,
) {
    when (event) {
        HapticEvent.ScanStart,
        HapticEvent.SafeStop,
        -> performHapticFeedback(HapticFeedbackType.LongPress)

        HapticEvent.ScanDone,
        HapticEvent.ReRead,
        -> performHapticFeedback(HapticFeedbackType.TextHandleMove)

        HapticEvent.Connected ->
            platform?.success() ?: performHapticFeedback(HapticFeedbackType.TextHandleMove)

        HapticEvent.Error ->
            platform?.error() ?: performHapticFeedback(HapticFeedbackType.LongPress)
    }
}
