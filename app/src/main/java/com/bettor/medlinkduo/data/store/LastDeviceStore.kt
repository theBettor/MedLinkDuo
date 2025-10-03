package com.bettor.medlinkduo.data.store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("app_prefs")

@Singleton
class LastDeviceStore @Inject constructor(@ApplicationContext private val ctx: Context) {
    private object Keys { val LAST_ID = stringPreferencesKey("last_device_id") }

    val lastId: Flow<String?> = ctx.dataStore.data.map { it[Keys.LAST_ID] }
    suspend fun setLast(id: String) { ctx.dataStore.edit { it[Keys.LAST_ID] = id } }
    suspend fun clear() { ctx.dataStore.edit { it.remove(Keys.LAST_ID) } }
}