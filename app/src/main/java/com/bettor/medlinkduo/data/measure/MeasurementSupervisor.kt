package com.bettor.medlinkduo.data.measure

import com.bettor.medlinkduo.core.di.IoDispatcher
import com.bettor.medlinkduo.domain.BleRepository
import com.bettor.medlinkduo.domain.Measurement
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class MeasurementSupervisor
    @Inject
    constructor(
        private val repo: BleRepository,
        @IoDispatcher private val io: CoroutineDispatcher,
    ) {
        private val gate = Mutex()
        private var job: Job? = null

        suspend fun start(onEach: (Measurement) -> Unit) =
            gate.withLock {
                job?.cancelAndJoin()
                job =
                    CoroutineScope(io + SupervisorJob()).launch {
                        try {
                            repo.measurements.collect { m ->
                                onEach(m)
                            }
                        } catch (_: CancellationException) {
                            // 정상 취소
                        }
                    }
            }

        suspend fun stop() =
            gate.withLock {
                job?.cancelAndJoin()
                job = null
            }
    }
