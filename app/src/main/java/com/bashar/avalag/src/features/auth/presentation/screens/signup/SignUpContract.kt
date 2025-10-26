package com.bashar.avalag.src.features.auth.presentation.screens.signup

import androidx.compose.runtime.Immutable

@Immutable
data class SignUpState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
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


sealed interface LoginEvent {
    data class EmailChanged(val value: String,) : LoginEvent
    data class PasswordChanged(val value: String) : LoginEvent
    data object TogglePasswordVisibility : LoginEvent
    data object Submit : LoginEvent
    data object ClearError : LoginEvent
    data class NavigateHome(val token: String) : LoginEvent
}
