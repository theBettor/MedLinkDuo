package com.bettor.medlinkduo.domain

import com.bettor.medlinkduo.data.measure.MeasurementSupervisor
import javax.inject.Inject
import javax.inject.Singleton

class StartMeasurementUseCase
    @Inject
    constructor(
        private val sup: MeasurementSupervisor,
    ) {
        suspend operator fun invoke(onEach: (Measurement) -> Unit) = sup.start(onEach)
    }

class PauseMeasurementUseCase
    @Inject
    constructor(
        private val sup: MeasurementSupervisor,
    ) {
        suspend operator fun invoke() = sup.stop()
    }

class EndMeasurementUseCase
    @Inject
    constructor(
        private val sup: MeasurementSupervisor,
    ) {
        suspend operator fun invoke(buildSummary: suspend () -> Unit = {}) {
            sup.stop()
            buildSummary()
        }
    }

/**
 * 측정값에서 숫자만 추출해 말하고 끝날 때까지 대기.
 * 예: "120.5 mmHg" -> "120.5" 만 발화
 */
@Singleton
class SpeakMeasurementNumericUseCase
    @Inject
    constructor(
        private val tts: TtsController,
    ) {
        suspend operator fun invoke(m: Measurement) {
            val spoken = m.value.filter { it.isDigit() || it == '.' || it == ',' }
            if (spoken.isNotBlank()) tts.speakAndWait(spoken)
        }
    }
