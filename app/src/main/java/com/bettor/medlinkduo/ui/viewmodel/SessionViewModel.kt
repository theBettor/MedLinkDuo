package com.bettor.medlinkduo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bettor.medlinkduo.core.common.MeasureUiState
import com.bettor.medlinkduo.core.common.Phase
import com.bettor.medlinkduo.domain.EndMeasurementUseCase
import com.bettor.medlinkduo.domain.Measurement
import com.bettor.medlinkduo.domain.PauseMeasurementUseCase
import com.bettor.medlinkduo.domain.SessionSummary
import com.bettor.medlinkduo.domain.StartMeasurementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val startUseCase: StartMeasurementUseCase,
    private val pauseUseCase: PauseMeasurementUseCase,
    private val endUseCase: EndMeasurementUseCase,
) : ViewModel() {

    private val _ui = MutableStateFlow(MeasureUiState())
    val ui: StateFlow<MeasureUiState> = _ui

    private val _last = MutableStateFlow<Measurement?>(null)
    val last: StateFlow<Measurement?> = _last

    private val _summary = MutableStateFlow<SessionSummary?>(null)
    val summary: StateFlow<SessionSummary?> = _summary

    @Volatile private var lastLocal: Measurement? = null

    fun remeasure() = viewModelScope.launch {
        if (_ui.value.busy || _ui.value.phase == Phase.Measuring) return@launch
        _ui.value = _ui.value.copy(busy = true)
        try {
            // ✅ 메인 스레드에 즉시 반영 + 로컬 스냅샷 동시 갱신
            startUseCase { m ->
                viewModelScope.launch(kotlinx.coroutines.Dispatchers.Main.immediate) {
                    _last.value = m
                    lastLocal = m
                }
            }
            _ui.value = MeasureUiState(phase = Phase.Measuring, busy = false)
        } finally {
            if (_ui.value.busy) _ui.value = _ui.value.copy(busy = false)
        }
    }

    fun pause() = viewModelScope.launch {
        if (_ui.value.busy || _ui.value.phase != Phase.Measuring) return@launch
        _ui.value = _ui.value.copy(busy = true)
        try {
            pauseUseCase()
            _ui.value = MeasureUiState(phase = Phase.Paused, busy = false)
        } finally {
            if (_ui.value.busy) _ui.value = _ui.value.copy(busy = false)
        }
    }

    fun end(onSummary: (Measurement?) -> Unit = {}) = viewModelScope.launch {
        if (_ui.value.busy || _ui.value.phase == Phase.Idle) return@launch
        _ui.value = _ui.value.copy(busy = true)
        try {
            // ✅ 스트림 정지 전 스냅샷 확정
            val snapshot = lastLocal ?: _last.value
            _summary.value = SessionSummary(last = snapshot)

            endUseCase {
                onSummary(snapshot)
            }
            _ui.value = MeasureUiState(phase = Phase.Idle, busy = false)
        } finally {
            if (_ui.value.busy) _ui.value = _ui.value.copy(busy = false)
        }
    }
}
