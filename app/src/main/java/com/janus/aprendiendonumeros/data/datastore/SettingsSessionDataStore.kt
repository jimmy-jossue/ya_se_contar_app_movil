package com.janus.aprendiendonumeros.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.janus.aprendiendonumeros.data.model.SettingsSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsSessionDataStore(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constant.SETTINGS_PREFERENCES_NAME)

    suspend fun getSettingsSession(): Flow<SettingsSession> =
        context.dataStore.data.map { preferences ->
            val id: String = preferences[USER_ID_KEY] ?: ""
            val saved: Boolean = preferences[SESSION_SAVED_KEY] ?: false

            SettingsSession(
                id = id,
                saved = saved
            )
        }

    suspend fun saveSession(session: SettingsSession) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = session.id
            preferences[SESSION_SAVED_KEY] = session.saved
        }
    }

    companion object {
        val USER_ID_KEY = stringPreferencesKey("user_id_key")
        val SESSION_SAVED_KEY = booleanPreferencesKey("session_saved_key")
    }
}