package com.bashar.avalag.src.features.setting.presentation.screens

import com.bashar.avalag.src.features.setting.domain.models.Language
import com.bashar.avalag.src.features.setting.domain.models.ThemeMode

data class SettingState(
    val theme: ThemeMode = ThemeMode.SYSTEM,
    val language: Language = Language.System,
    val biometricEnabled: Boolean = false // hook up later
)

sealed class SettingsEvents {

//    data class ShowSnackBar(val message: String): TemplateEvents()
    data class OnCheckChange(val checked: Boolean) : SettingsEvents()
    object OnBackPress : SettingsEvents()
    object Refresh : SettingsEvents()
    object OnOpenNotifications : SettingsEvents()
    object OnChangePassword : SettingsEvents()
    object OnOpenPolicy : SettingsEvents()
    object OnOpenLanguagePicker : SettingsEvents()
    object OnOpenThemePicker : SettingsEvents()
//    data class Search(val query: String) : TemplateEvents()


}