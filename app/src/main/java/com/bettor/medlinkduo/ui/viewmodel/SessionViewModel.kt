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
class SessionViewModel
    @Inject
    constructor(
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

        fun remeasure() =
            viewModelScope.launch {
                if (_ui.value.busy || _ui.value.phase == Phase.Measuring) return@launch
                _ui.value = _ui.value.copy(busy = true)
                try {
                    // 측정 스트림 시작: onEach에서 마지막 값 갱신/저장
                    startUseCase { m -> _last.value = m } // ← UseCase 호출은 명확히
                    _ui.value = MeasureUiState(phase = Phase.Measuring, busy = false)
                } finally {
                    if (_ui.value.busy) _ui.value = _ui.value.copy(busy = false)
                }
            }

        fun pause() =
            viewModelScope.launch {
                if (_ui.value.busy || _ui.value.phase != Phase.Measuring) return@launch
                _ui.value = _ui.value.copy(busy = true)
                try {
                    pauseUseCase() // ← 기존엔 ‘pause’ 자체(프로퍼티)여서 호출 안 됨/재귀 위험
                    _ui.value = MeasureUiState(phase = Phase.Paused, busy = false)
                } finally {
                    if (_ui.value.busy) _ui.value = _ui.value.copy(busy = false)
                }
            }

    fun end(onSummary: (Measurement?) -> Unit = {}) =
        viewModelScope.launch {
            if (_ui.value.busy || _ui.value.phase == Phase.Idle) return@launch
            _ui.value = _ui.value.copy(busy = true)
            try {
                // ✅ 스트림을 멈추기 전에 '현재 값'을 스냅샷
                val snapshot = _last.value
                _summary.value = SessionSummary(last = snapshot)

                // 이후 실제 종료 로직 진행 (repo/서비스 stop)
                endUseCase {
                    // 콜백이 늦게 와도 이미 _summary 에는 스냅샷이 들어가 있음
                    onSummary(snapshot)
                }

                _ui.value = MeasureUiState(phase = Phase.Idle, busy = false)
            } finally {
                if (_ui.value.busy) _ui.value = _ui.value.copy(busy = false)
            }
        }
    }
