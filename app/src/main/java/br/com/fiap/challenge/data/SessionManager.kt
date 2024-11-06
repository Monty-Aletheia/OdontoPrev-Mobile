package br.com.fiap.challenge.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val USER_TOKEN = stringPreferencesKey("user_token")
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


object SessionManager {

    suspend fun getSessionToken(context: Context): String {
        val userTokenFlow: Flow<String> = context.dataStore.data
            .map { settings ->
                settings[USER_TOKEN] ?: "Null"
            }

        return userTokenFlow.first()

    }

    suspend fun saveSessionToken(context: Context, token: String) {
        context.dataStore.edit { settings ->
            settings[USER_TOKEN] = token
        }
    }

}