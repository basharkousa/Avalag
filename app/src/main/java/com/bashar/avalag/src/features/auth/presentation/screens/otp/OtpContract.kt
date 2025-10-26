package com.bashar.avalag.src.features.auth.presentation.screens.otp

import androidx.compose.runtime.Immutable

@Immutable
data class OtpState(
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


sealed interface OtpEvent {
    data class EmailChanged(val value: String,) : OtpEvent
    data class PasswordChanged(val value: String) : OtpEvent
    data object TogglePasswordVisibility : OtpEvent
    data object Submit : OtpEvent
    data object ClearError : OtpEvent
    data class NavigateHome(val token: String) : OtpEvent
}
