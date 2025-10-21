package com.bashar.avalag.src.features.setting.data

import com.bashar.avalag.src.features.setting.data.local.datastore.SettingPreferencesDataSource
import com.bashar.avalag.src.features.setting.domain.models.Language
import com.bashar.avalag.src.features.setting.domain.models.ThemeMode
import com.bashar.avalag.src.features.setting.domain.repositories.ISettingRepo


class SettingRepo(
    private val ds: SettingPreferencesDataSource
) : ISettingRepo {

    override fun observeTheme() = ds.theme
    override suspend fun setTheme(mode: ThemeMode) = ds.setTheme(mode)

    override fun observeLanguage() = ds.language
    override suspend fun setLanguage(language: Language) = ds.setLanguage(language)
}
