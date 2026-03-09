package com.bashar.avalag.src.features.setting.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bashar.avalag.src.features.setting.domain.models.Language
import com.bashar.avalag.src.features.setting.domain.models.ThemeMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingPreferencesDataSource(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val THEME = stringPreferencesKey("theme_mode")
        val LANG  = stringPreferencesKey("language")

        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
    }

    suspend fun isFirstLaunch() = dataStore.data.first()[Keys.FIRST_LAUNCH]?:true


    suspend fun setFirstLaunch(value: Boolean) = dataStore.edit { it[Keys.FIRST_LAUNCH] = value }


    val theme = dataStore.data.map { prefs ->
        ThemeMode.valueOf(prefs[Keys.THEME] ?: ThemeMode.SYSTEM.name)
    }

    val language = dataStore.data.map { prefs ->
        Language.fromTag(prefs[Keys.LANG])
    }

    suspend fun setTheme(mode: ThemeMode) = dataStore.edit { it[Keys.THEME] = mode.name }
    suspend fun setLanguage(language: Language) = dataStore.edit { it[Keys.LANG] = language.tag }
}
