package com.bashar.avalag.src.features.auth.domain.validation

import com.bashar.avalag.src.core.utils.UiText
import com.bashar.avalag.R

object AuthValidator {

    fun validatePhone(phone: String): UiText? {
        val value = phone.trim()

        if (value.isBlank()) {
            return UiText.StringResource(R.string.phone_number_required)
        }

        if (!value.all { it.isDigit() }) {
            return UiText.StringResource(R.string.phone_number_invalid)
        }

        if (value.length < 6) {
            return UiText.StringResource(R.string.phone_number_too_short)
        }

        return null
    }

    fun validatePassword(password: String): UiText? {
        val value = password.trim()

        if (value.isBlank()) {
            return UiText.StringResource(R.string.password_required)
        }

        if (value.length < 6) {
            return UiText.StringResource(R.string.password_too_short)
        }

        return null
    }
}