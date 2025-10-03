package com.bettor.medlinkduo.core.common

enum class Phase { Idle, Measuring, Paused }

data class MeasureUiState(
    val phase: Phase = Phase.Idle,
    val busy: Boolean = false
)