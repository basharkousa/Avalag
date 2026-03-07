package com.bashar.avalag.src.features.auth.presentation.screens.login

import com.bashar.avalag.src.core.utils.UiText
import com.bashar.avalag.src.features.auth.presentation.data.AuthCountriesProvider
import com.bashar.avalag.src.features.auth.presentation.model.AuthCountryUi

data class LoginState(
    val username: String = "",
    val password: String = "",
    val selectedCountry: AuthCountryUi = AuthCountriesProvider.defaultCountry,
    val availableCountries: List<AuthCountryUi> = AuthCountriesProvider.countries,
    val isLoading: Boolean = false,
    val snackbarMessage: UiText? = null,
    val navigateTo: LoginDestination? = null
)

enum class LoginDestination {
    MAIN,
    SIGNUP,
    RESET_PASSWORD,
    BACK,
    SKIP
}

/** UI -> Route */
sealed interface LoginUiEvent {
    data class UsernameChanged(val value: String) : LoginUiEvent
    data class PasswordChanged(val value: String) : LoginUiEvent
    data class CountryChanged(val value: AuthCountryUi) : LoginUiEvent

    data object LoginClicked : LoginUiEvent
    data object BackClicked : LoginUiEvent
    data object SkipClicked : LoginUiEvent

    data object ConsumeSnackbar : LoginUiEvent
    data class Navigate(val to: LoginDestination) : LoginUiEvent
}

/** Route -> VM */
sealed interface LoginEvents {
    data class UsernameChanged(val value: String) : LoginEvents
    data class PasswordChanged(val value: String) : LoginEvents
    data class CountryChanged(val value: AuthCountryUi) : LoginEvents

    data object LoginClicked : LoginEvents
    data object ConsumeSnackbar : LoginEvents
}