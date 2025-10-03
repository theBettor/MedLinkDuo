package com.bettor.medlinkduo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bettor.medlinkduo.data.local.SaveFeedbackUseCase
import com.bettor.medlinkduo.workers.SyncLogsWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val save: SaveFeedbackUseCase,
    private val work: WorkManager
) : ViewModel() {
    fun submit(rating: Int, category: String, comment: String) = viewModelScope.launch {
        save(rating, category, comment)
        val input = Data.Builder()
            .putString(SyncLogsWorker.KEY_SESSION_ID, System.currentTimeMillis().toString())
            .putInt(SyncLogsWorker.KEY_RATING, rating)
            .putString(SyncLogsWorker.KEY_CATEGORY, category)
            .build()

        work.enqueue(SyncLogsWorker.request(input))
    }
}
