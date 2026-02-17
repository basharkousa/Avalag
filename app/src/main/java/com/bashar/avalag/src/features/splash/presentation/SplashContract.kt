package com.bashar.avalag.src.features.splash.presentation

import com.bashar.avalag.src.core.utils.UiText

data class SplashState(
    val isLoading: Boolean = true,
    val updateDialog: UpdateDialogState? = null,
    val snackbarMessage: UiText? = null,
    val navigateTo: SplashDestination? = null
)

data class UpdateDialogState(val link: String?)

enum class SplashDestination { AUTH, MAIN }

/** UI -> Route */
sealed interface SplashUiEvent {
    data object Retry : SplashUiEvent
    data object ConsumeSnackbar : SplashUiEvent
    data object OpenUpdateLink : SplashUiEvent
    data object ExitApp : SplashUiEvent
    data class Navigate(val to: SplashDestination) : SplashUiEvent
}

/** Route -> VM */
sealed interface SplashEvents {
    data object Retry : SplashEvents
    data object ConsumeSnackbar : SplashEvents
}
