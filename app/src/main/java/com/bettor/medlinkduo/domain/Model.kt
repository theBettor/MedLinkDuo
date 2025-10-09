package com.bettor.medlinkduo.domain

data class BleDevice(val id: String, val name: String?, val rssi: Int)

data class Measurement(val value: String, val unit: String, val ts: Long)

sealed interface ConnectionState {
    data object Idle : ConnectionState

    data object Scanning : ConnectionState

    data class Connecting(val device: BleDevice) : ConnectionState

    data class Discovering(val device: BleDevice) : ConnectionState

    data class Synced(val device: BleDevice) : ConnectionState

    data class Disconnected(val reason: String) : ConnectionState
}

data class SessionSummary(
    val last: Measurement?,
    val count: Int = if (last != null) 1 else 0,
    val note: String? = null,
)
