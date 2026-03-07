package com.bashar.avalag.src.features.auth.presentation.screens.login

import com.bashar.avalag.src.core.utils.UiText

data class LoginState(
    val username: String = "",
    val key: String = "+963",
    val password: String = "",
    val isLoading: Boolean = false,
    val snackbarMessage: UiText? = null,
    val navigateTo: LoginDestination? = null
)

enum class LoginDestination {
    MAIN,
    SIGNUP,
    RESET_PASSWORD,
    BACK,
    SKIP,

}

/** UI -> Route */
sealed interface LoginUiEvent {
    data class UsernameChanged(val value: String) : LoginUiEvent
    data class PasswordChanged(val value: String) : LoginUiEvent

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
    data object LoginClicked : LoginEvents
    data object ConsumeSnackbar : LoginEvents
}