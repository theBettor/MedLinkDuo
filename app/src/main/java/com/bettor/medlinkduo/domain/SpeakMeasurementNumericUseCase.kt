package com.bettor.medlinkduo.domain

import javax.inject.Inject
import javax.inject.Singleton


/**
 * 측정값에서 숫자만 추출해 말하고 끝날 때까지 대기.
 * 예: "120.5 mmHg" -> "120.5" 만 발화
 */
@Singleton
class SpeakMeasurementNumericUseCase @Inject constructor(
    private val tts: TtsController
) {
    suspend operator fun invoke(m: Measurement) {
        val spoken = m.value.filter { it.isDigit() || it == '.' || it == ',' }
        if (spoken.isNotBlank()) tts.speakAndWait(spoken)
    }
}