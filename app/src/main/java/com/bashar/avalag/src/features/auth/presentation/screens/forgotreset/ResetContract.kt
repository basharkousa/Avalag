package com.bashar.avalag.src.features.auth.presentation.screens.forgotreset

import androidx.compose.runtime.Immutable

@Immutable
data class ResetState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val isEmailValid: Boolean get() = EMAIL_REGEX.matches(email.trim())
    val isPasswordValid: Boolean get() = password.length >= 6
    val canSubmit: Boolean get() = isEmailValid && isPasswordValid && !isLoading


    companion object {
        private val EMAIL_REGEX =
            """^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$""".toRegex(RegexOption.IGNORE_CASE)
    }
}


sealed interface ResetEvent {
    data class EmailChanged(val value: String,) : ResetEvent
    data class PasswordChanged(val value: String) : ResetEvent
    data object TogglePasswordVisibility : ResetEvent
    data object Submit : ResetEvent
    data object ClearError : ResetEvent
    data class NavigateHome(val token: String) : ResetEvent
}
