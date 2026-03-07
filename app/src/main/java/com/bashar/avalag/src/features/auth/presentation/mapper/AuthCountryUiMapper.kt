package com.bashar.avalag.src.features.auth.presentation.mapper

import com.bashar.avalag.src.core.ui.widgets.CountryUi
import com.bashar.avalag.src.features.auth.presentation.model.AuthCountryUi

fun AuthCountryUi.toWidgetModel(): CountryUi {
    return CountryUi(
        code = code,
        name = name,
        flagRes = flagRes
    )
}

fun CountryUi.toAuthModel(): AuthCountryUi {
    return AuthCountryUi(
        code = code,
        name = name,
        flagRes = flagRes
    )
}