package com.bashar.avalag.src.features.auth.presentation.model

import androidx.annotation.DrawableRes

data class AuthCountryUi(
    val iso2: String,
    val dialCode: String,
    val name: String,
    val flagEmoji: String
)