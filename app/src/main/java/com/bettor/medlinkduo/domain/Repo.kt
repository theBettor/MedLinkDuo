package com.bettor.medlinkduo.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BleRepository {
    val connectionState: StateFlow<ConnectionState>
    val measurements: Flow<Measurement>
    suspend fun scan(): Flow<List<BleDevice>>
    suspend fun connect(d: BleDevice)
    suspend fun disconnect()
}


interface TtsController {
    /** 큐를 비우고 즉시 말함(대기 없음) */
    fun speak(text: String)

    /** 이 발화가 끝날 때까지 suspend (UtteranceProgressListener 기반) */
    suspend fun speakAndWait(text: String)

    /** 현재 발화 중지 */
    fun stop()

    /** 엔진 종료(앱 종료 등) */
    fun shutdown()
}