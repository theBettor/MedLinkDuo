package com.bettor.medlinkduo.data.store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("onboarding")

@Singleton
class OnboardingStore @Inject constructor(@ApplicationContext private val ctx: Context) {
    private object Keys { val SPOKEN = booleanPreferencesKey("spoken_v1") }

    suspend fun shouldSpeak(): Boolean =
        ctx.dataStore.data.map { it[Keys.SPOKEN] != true }.first()

    suspend fun markSpoken() {
        ctx.dataStore.edit { it[Keys.SPOKEN] = true }
    }
}