package com.bashar.avalag.src.features.setting.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bashar.avalag.src.core.ui.widgets.topbars.DefaultTppBar
import com.bashar.avalag.src.features.setting.domain.models.Language
import com.bashar.avalag.src.features.setting.domain.models.ThemeMode
import com.bashar.avalag.src.core.ui.widgets.items.ChevronItem
import com.bashar.avalag.R
import com.bashar.avalag.src.features.setting.presentation.widgets.dialogs.LanguagePickerDialog
import com.bashar.avalag.src.features.setting.presentation.widgets.dialogs.ThemePickerDialog
import com.bashar.avalag.src.features.setting.presentation.widgets.items.SwitchItem
import com.bashar.avalag.src.features.setting.presentation.widgets.items.ValueItem
import com.bashar.avalag.src.features.setting.presentation.widgets.sections.CardSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onBack: () -> Unit = {},
    onOpenNotifications: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onOpenPolicy: () -> Unit = {},
    vm: SettingsViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    var showTheme by remember { mutableStateOf(false) }
    var showLang by remember { mutableStateOf(false) }

    ScreenContent(
        state = state,
        onEvent = { event ->
            when(event) {
                is SettingsEvents.OnBackPress -> onBack()
//                else -> viewModel.onEvent(event)
                SettingsEvents.OnChangePassword -> onChangePassword()
                is SettingsEvents.OnCheckChange -> {

                }
                SettingsEvents.OnOpenNotifications -> onOpenNotifications()
                SettingsEvents.OnOpenPolicy -> {
                    onOpenPolicy()
                }
                SettingsEvents.OnOpenLanguagePicker -> {
                    showLang = true
                }
                SettingsEvents.OnOpenThemePicker -> {
                    showTheme = true
                }
                SettingsEvents.Refresh -> {

                }
            }
        }
    )

    if (showTheme) ThemePickerDialog(
        selected = state.theme,
        onSelect = { vm.onThemeSelected(it); showTheme = false },
        onDismiss = { showTheme = false }
    )
    if (showLang) LanguagePickerDialog(
        selected = state.language,
        onSelect = { vm.onLanguageSelected(it); showLang = false },
        onDismiss = { showLang = false }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ScreenContent(
    state: SettingState = SettingState(),
    onEvent: (SettingsEvents) -> Unit = {}
) {

    LaunchedEffect(Unit) {
//        if(!state.isLoading) {
//            onEvent(ScreenEvents.NavigateToScreen)
//        }
    }
    Scaffold(
        topBar = {
            DefaultTppBar(stringResource = R.string.settings, onBack = {onEvent(SettingsEvents.OnBackPress)})
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.background.copy())
                .padding(12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CardSection(stringResource(R.string.notifications)) {
                ChevronItem(
                    R.drawable.ic_ring,
                    stringResource(R.string.notifications_setting),
                    stringResource(R.string.manage_your_notifications_preferences),

                ){
                    onEvent(SettingsEvents.OnOpenNotifications)
                }
            }
            CardSection(stringResource(R.string.security)) {
                ChevronItem(R.drawable.ic_lock, stringResource(R.string.change_password), onClick = {onEvent(SettingsEvents.OnChangePassword)})
                SwitchItem(R.drawable.ic_fingerprint, stringResource(R.string.biometric_authentications),
                    checked = state.biometricEnabled, onCheckedChange = { onEvent(SettingsEvents.OnCheckChange(it))  })
                ChevronItem(R.drawable.ic_document, stringResource(R.string.privacy_policy), onClick = {onEvent(SettingsEvents.OnOpenPolicy)})
            }
            CardSection(stringResource(R.string.preferences)) {
                ValueItem(
                    icon = Icons.Outlined.Translate,
                    title = stringResource(R.string.change_language),
                    value = when (state.language) {
                        Language.System -> stringResource(R.string.system_default)
                        else -> state.language.name
                    },
                    onClick = { onEvent(SettingsEvents.OnOpenLanguagePicker) }
                )
                ValueItem(
                    icon = Icons.Outlined.DarkMode,
                    title = stringResource(R.string.theme_mode),
                    value = when (state.theme) {
                        ThemeMode.SYSTEM -> stringResource(R.string.system)
                        ThemeMode.LIGHT -> stringResource(R.string.light)
                        ThemeMode.DARK -> stringResource(R.string.dark)
                    },
                    onClick = { onEvent(SettingsEvents.OnOpenThemePicker) }
                )
            }
        }
    }

}






