package com.bashar.avalag.src.features.setting.presentation.screens

import com.bashar.avalag.src.features.setting.domain.models.Language
import com.bashar.avalag.src.features.setting.domain.models.ThemeMode

data class SettingState(
    val theme: ThemeMode = ThemeMode.SYSTEM,
    val language: Language = Language.System,
    val biometricEnabled: Boolean = false // hook up later
)

sealed class SettingsEvent {

//    data class ShowSnackBar(val message: String): TemplateEvents()
    data class ToggleBiometrics(val enabled: Boolean) : SettingsEvent()
    object OnBackPress : SettingsEvent()
    object Refresh : SettingsEvent()
    object OnOpenNotifications : SettingsEvent()
    object OnChangePassword : SettingsEvent()
    object OnOpenPolicy : SettingsEvent()
    object OnOpenLanguagePicker : SettingsEvent()
    object OnOpenThemePicker : SettingsEvent()
//    data class Search(val query: String) : TemplateEvents()


}