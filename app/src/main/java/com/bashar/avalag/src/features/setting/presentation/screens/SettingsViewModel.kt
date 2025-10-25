package com.bashar.avalag.src.features.setting.presentation.screens


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bashar.avalag.src.features.setting.domain.*
import com.bashar.avalag.src.features.setting.domain.models.Language
import com.bashar.avalag.src.features.setting.domain.models.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getTheme: GetThemeModeFlowUseCase,
    getLanguage: GetLanguageFlowUseCase,
    private val setTheme: SetThemeModeUseCase,
    private val setLanguage: SetLanguageUseCase,
    private val applyLanguage: ApplyLanguageUseCase
) : ViewModel() {

    val state: StateFlow<SettingState> =
        combine(getTheme(), getLanguage()) { theme, lang ->
            SettingState(theme = theme, language = lang)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingState())

    fun onThemeSelected(mode: ThemeMode) = viewModelScope.launch {
        setTheme(mode)
    }

    fun onLanguageSelected(language: Language) = viewModelScope.launch {
        setLanguage(language)      // persist
        applyLanguage(language)    // side-effect: switch app locales
    }


}
