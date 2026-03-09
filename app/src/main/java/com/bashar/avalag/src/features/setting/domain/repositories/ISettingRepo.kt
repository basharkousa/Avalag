package com.bashar.avalag.src.features.setting.domain.repositories

import androidx.datastore.preferences.core.Preferences
import com.bashar.avalag.src.features.setting.domain.models.Language
import com.bashar.avalag.src.features.setting.domain.models.ThemeMode
import kotlinx.coroutines.flow.Flow


interface ISettingRepo {
    fun observeTheme(): Flow<ThemeMode>
    suspend fun setTheme(mode: ThemeMode): Preferences

    fun observeLanguage(): Flow<Language>
    suspend fun setLanguage(language: Language): Preferences

    suspend fun isFirstLaunch(): Boolean

    suspend fun setFirstLaunch(value: Boolean)
}
