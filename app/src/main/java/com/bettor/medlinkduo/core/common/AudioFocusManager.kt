package com.bettor.medlinkduo.core.common

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build

class AudioFocusManager(context: Context) {
    private val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var afr: AudioFocusRequest? = null
    private val listener = AudioManager.OnAudioFocusChangeListener { /* 필요시 반응 */ }

    fun request(): Boolean {
        return if (Build.VERSION.SDK_INT >= 26) {
            val req =
                AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                    .setOnAudioFocusChangeListener(listener)
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build(),
                    ).build()
            afr = req
            am.requestAudioFocus(req) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            am.requestAudioFocus(
                listener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK,
            ) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }
    }

    fun abandon() {
        if (Build.VERSION.SDK_INT >= 26) {
            afr?.let { am.abandonAudioFocusRequest(it) }
        } else {
            am.abandonAudioFocus(listener)
        }
    }
}
