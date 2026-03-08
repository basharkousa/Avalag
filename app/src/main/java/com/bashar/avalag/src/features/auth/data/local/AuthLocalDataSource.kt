package com.bashar.avalag.src.features.auth.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bashar.avalag.src.features.auth.domain.repositories.IAuthLocalDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : IAuthLocalDataSource {

    private object Keys {
        val TOKEN = stringPreferencesKey("auth_token")
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { prefs ->
            prefs[Keys.TOKEN] = token
        }
    }

    override suspend fun getToken(): String? {
        val prefs = dataStore.data.first()
        return prefs[Keys.TOKEN]
    }

    override suspend fun clearToken() {
        dataStore.edit { prefs ->
            prefs.remove(Keys.TOKEN)
        }
    }
}