package com.bettor.medlinkduo.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SyncLogsWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        // 입력값 예시(없어도 동작)
        val sessionId = inputData.getString(KEY_SESSION_ID) ?: "demo-session"
        val rating = inputData.getInt(KEY_RATING, -1)
        val category = inputData.getString(KEY_CATEGORY) ?: "unknown"

        Timber.d("SyncLogsWorker start: session=%s rating=%d category=%s", sessionId, rating, category)

        return try {
            // --- 모킹 업로드 ---
            delay(800) // pretend network upload
            Timber.d("SyncLogsWorker success for session=%s", sessionId)
            Result.success()
        } catch (t: Throwable) {
            Timber.e(t, "SyncLogsWorker failed, will retry")
            Result.retry()
        }
    }

    companion object {
        const val TAG = "SyncLogsWorker"

        // Input keys (원하면 안 써도 됨)
        const val KEY_SESSION_ID = "session_id"
        const val KEY_RATING = "rating"
        const val KEY_CATEGORY = "category"

        fun constraints(): Constraints =
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // 네트워크 있을 때만
                .build()

        fun request(input: Data = Data.EMPTY): OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<SyncLogsWorker>()
                .setInputData(input)
                .setConstraints(constraints())
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
                .addTag(TAG)
                .build()

        fun enqueue(
            workManager: WorkManager,
            input: Data = Data.EMPTY,
        ) {
            workManager.enqueue(request(input))
        }
    }
}
