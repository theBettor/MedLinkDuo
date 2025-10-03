package com.bettor.medlinkduo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bettor.medlinkduo.data.local.MeasurementDao
import com.bettor.medlinkduo.data.local.MeasurementEntity
import com.bettor.medlinkduo.data.local.ObserveMeasurementsUseCase
import com.bettor.medlinkduo.domain.Measurement
import com.bettor.medlinkduo.domain.SessionSummary
import com.bettor.medlinkduo.domain.SpeakMeasurementNumericUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val observe: ObserveMeasurementsUseCase,
    private val measurementDao: MeasurementDao,
    private val speakNumeric: SpeakMeasurementNumericUseCase
) : ViewModel() {

    private val _last = MutableStateFlow<Measurement?>(null)
    val last: StateFlow<Measurement?> = _last

    private val _isMeasuring = MutableStateFlow(false)
    val isMeasuring: StateFlow<Boolean> = _isMeasuring

    private val _summary = MutableStateFlow<SessionSummary?>(null)
    val summary: StateFlow<SessionSummary?> = _summary

    private var observeJob: Job? = null

    /** 측정 시작: 이미 진행 중이면 무시. */
    fun startMeasurement() {
        if (_isMeasuring.value) return
        _isMeasuring.value = true
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            // 새 측정이 오면 이전 발화는 cancel 되고 최신만 낭독됨(collectLatest)
            observe().collectLatest { m ->
                _last.value = m
                measurementDao.upsert(MeasurementEntity(ts = m.ts, value = m.value, unit = m.unit))
                speakNumeric(m)  // ⬅️ 숫자만 낭독 + 끝날 때까지 대기
            }
        }
    }

    fun pause() {
        _isMeasuring.value = false
        observeJob?.cancel()
        observeJob = null
    }

    /** 종료: 측정 중단 + 요약 생성(화면/데이터는 유지) */
    fun end() {
        pause()
        _summary.value = SessionSummary(last = _last.value)
        // _last 는 초기화하지 않음 → MeasurementScreen에 그대로 표시됨
    }

    fun remeasure() {
        pause()
        startMeasurement()
    }

    override fun onCleared() {
        observeJob?.cancel()
    }

    // --- 매핑 유틸 ---
    private fun Measurement.toEntity() = MeasurementEntity(
        ts = ts,
        value = value,
        unit = unit
    )
}