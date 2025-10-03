package com.bettor.medlinkduo.data.ble


import android.content.Context
import com.bettor.medlinkduo.domain.BleDevice
import com.bettor.medlinkduo.domain.BleRepository
import com.bettor.medlinkduo.domain.ConnectionState
import com.bettor.medlinkduo.domain.Measurement
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Random

@Singleton
class MockBleRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val scope: CoroutineScope
) : BleRepository {

    private val _state = MutableStateFlow<ConnectionState>(ConnectionState.Idle)
    override val connectionState: StateFlow<ConnectionState> = _state

    private val _measurements = MutableSharedFlow<Measurement>(replay = 0, extraBufferCapacity = 16)
    override val measurements: Flow<Measurement> = _measurements.asSharedFlow()

    private var notifyJob: Job? = null
    private val rnd = Random(System.currentTimeMillis())

    /** inclusive 범위 난수 (min..maxInclusive) */
    private fun rand(min: Int, maxInclusive: Int): Int {
        require(maxInclusive >= min) { "maxInclusive($maxInclusive) < min($min)" }
        val span = maxInclusive - min + 1
        return rnd.nextInt(span) + min
    }

    override suspend fun scan(): Flow<List<BleDevice>> = flow {
        _state.value = ConnectionState.Scanning

        try {
            repeat(4) {
                delay(350)
                emit(
                    listOf(
                        BleDevice("AA:BB:01", "HIMS-01", (-75..-50).random()),
                        BleDevice("AA:BB:02", "HIMS-02", (-80..-55).random())
                    )
                )
            }
        } finally {
            // ⬇️ 스캔 스트림 종료 시 스캔 상태 해제
            _state.value = ConnectionState.Idle
        }
    }

    override suspend fun connect(d: BleDevice) {
        _state.value = ConnectionState.Connecting(d); delay(400)
        _state.value = ConnectionState.Discovering(d); delay(300)
        _state.value = ConnectionState.Synced(d)

        notifyJob?.cancel()
        notifyJob = scope.launch {
            while (isActive && connectionState.value is ConnectionState.Synced) {
                _measurements.emit(
                    Measurement(
                        value = rand(90, 130).toString(), // 90..130
                        unit = "mmHg",
                        ts = System.currentTimeMillis()
                    )
                )
                delay(1500)

                // 1/21 확률로 드롭
                if (rnd.nextInt(21) == 0) {
                    _state.value = ConnectionState.Disconnected("drop")
                    backoffReconnect(d)
                }
            }
        }
    }

    private suspend fun backoffReconnect(d: BleDevice) {
        for (ms in longArrayOf(500L, 1000L, 2000L)) {
            _state.value = ConnectionState.Connecting(d); delay(ms)
            _state.value = ConnectionState.Discovering(d); delay(200)
            _state.value = ConnectionState.Synced(d)
            if (connectionState.value is ConnectionState.Synced) return
        }
        _state.value = ConnectionState.Disconnected("failed")
    }

    override suspend fun disconnect() {
        notifyJob?.cancel()
        _state.value = ConnectionState.Disconnected("manual")
    }
}