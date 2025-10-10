package com.bettor.medlinkduo.core.ui

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class SensoryFeedback(private val ctx: Context) {
    private val tone = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80)

    fun tick() {
        tone.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
    }

    fun success() {
        tone.startTone(ToneGenerator.TONE_PROP_BEEP2, 120)
    }

    fun error() {
        tone.startTone(ToneGenerator.TONE_SUP_ERROR, 200)
    }

    fun vibrate(ms: Long = 40) {
        runCatching {
            val vib: Vibrator =
                if (Build.VERSION.SDK_INT >= 31) {
                    (ctx.getSystemService(VibratorManager::class.java)).defaultVibrator
                } else {
                    ctx.getSystemService(Vibrator::class.java)
                }
            if (Build.VERSION.SDK_INT >= 26) {
                vib.vibrate(VibrationEffect.createOneShot(ms, 160))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(ms)
            }
        }.onFailure {
            // 선택: 로그만 남기고 무시하거나, Compose 하프틱으로 폴백하도록 설계해도 됨
            // Timber.w(it, "vibrate failed; falling back to UI haptics")
        }
    }
}
