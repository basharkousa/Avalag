package com.bashar.avalag.src.features.auth.presentation.data

import com.bashar.avalag.R
import com.bashar.avalag.src.features.auth.presentation.model.AuthCountryUi

object AuthCountriesProvider {

    val countries = listOf(
        AuthCountryUi(code = "+963", name = "Syria", flagRes = R.drawable.ic_ring),
        AuthCountryUi(code = "+974", name = "Qatar", flagRes = R.drawable.ic_ring),
        AuthCountryUi(code = "+966", name = "Saudi Arabia", flagRes = R.drawable.ic_ring),
        AuthCountryUi(code = "+971", name = "United Arab Emirates", flagRes = R.drawable.ic_ring),
    )

    val defaultCountry: AuthCountryUi
        get() = countries.first()
}