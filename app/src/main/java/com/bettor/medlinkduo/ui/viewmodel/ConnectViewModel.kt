package com.bettor.medlinkduo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bettor.medlinkduo.domain.BleDevice
import com.bettor.medlinkduo.domain.BleRepository
import com.bettor.medlinkduo.domain.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/** 말하기 큐에 실어 보낼 항목 */
data class Speech(
    val text: String,
    /** true면 speakAndWait, false면 speak(비대기) */
    val await: Boolean = true,
    /** 재생이 끝난 뒤 UI가 처리할 태그(네비게이션 등) */
    val tag: SpeechTag? = null,
)

enum class SpeechTag { NavigateToMeas }

@HiltViewModel
class ConnectViewModel
    @Inject
    constructor(
        private val repo: BleRepository,
    ) : ViewModel() {
        private val _devices = MutableStateFlow<List<BleDevice>>(emptyList())
        val devices: StateFlow<List<BleDevice>> = _devices

        val connection: StateFlow<ConnectionState> = repo.connectionState

        private val _scanPhase = MutableStateFlow("Idle")
        val scanPhase: StateFlow<String> = _scanPhase

        // --- 말하기 큐 (UI에서 collect하여 실제 재생) ---
        private val _speech = MutableSharedFlow<Speech>(extraBufferCapacity = 32)
        val speech: SharedFlow<Speech> = _speech.asSharedFlow()

        private var scanJob: Job? = null
        private var announcedSynced = false

        init {
            // 연결 완료 알림 → 멘트 끝난 뒤 이동하도록 태그를 붙여 큐에 싣는다
            viewModelScope.launch {
                connection.collect { state ->
                    if (state is ConnectionState.Synced && !announcedSynced) {
                        announcedSynced = true
                        say("연결되었습니다. 측정 화면으로 이동합니다.", await = true, tag = SpeechTag.NavigateToMeas)
                    }
                    if (state !is ConnectionState.Synced) {
                        announcedSynced = false
                    }
                }
            }
        }

        /** 화면 진입/복귀 시 자동 스캔 보장 */
        fun ensureScan() {
            if (connection.value !is ConnectionState.Synced && _scanPhase.value != "Scanning") {
                onScan()
            }
        }

        fun onScan() {
            if (_scanPhase.value == "Scanning") return
            scanJob?.cancel()
            scanJob =
                viewModelScope.launch {
                    _scanPhase.value = "Scanning"
                    // 스캔 시작 안내는 끊지 않고 붙여도 되지만, 겹침 방지를 위해 대기 없이 한 줄만
                    say("주변 기기를 찾는 중입니다", await = false)
                    try {
                        repo.scan().collect { list -> _devices.value = list }
                    } finally {
                        _scanPhase.value = "Done"
                        if (connection.value !is ConnectionState.Synced) {
                            say("스캔이 완료되었습니다. 원하시는 기기를 선택하여 주십시오.", await = true)
                        }
                    }
                }
        }

        fun onConnect(d: BleDevice) = viewModelScope.launch { repo.connect(d) }

        private fun say(
            text: String,
            await: Boolean = true,
            tag: SpeechTag? = null,
        ) {
            _speech.tryEmit(Speech(text, await, tag))
        }
    }
