package com.bashar.avalag.src.features.auth.presentation.mapper

import com.bashar.avalag.src.core.ui.widgets.CountryUi
import com.bashar.avalag.src.features.auth.presentation.model.AuthCountryUi

fun AuthCountryUi.toWidgetModel(): CountryUi {
    return CountryUi(
        code = dialCode,
        name = name,
        flagEmoji = flagEmoji
    )
}

fun CountryUi.toAuthModel(selectedIso2: String? = null): AuthCountryUi {
    return AuthCountryUi(
        iso2 = selectedIso2 ?: "",
        dialCode = code,
        name = name,
        flagEmoji = flagEmoji
    )
}