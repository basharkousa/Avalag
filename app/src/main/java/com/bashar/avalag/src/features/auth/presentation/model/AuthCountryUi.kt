package com.bashar.avalag.src.features.auth.presentation.model

import androidx.annotation.DrawableRes

data class AuthCountryUi(
    val code: String,
    val name: String,
    @DrawableRes val flagRes: Int
)