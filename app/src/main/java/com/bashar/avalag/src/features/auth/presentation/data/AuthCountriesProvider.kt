package com.bashar.avalag.src.features.auth.presentation.data

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.bashar.avalag.src.features.auth.presentation.model.AuthCountryUi
import java.util.Locale

object AuthCountriesProvider {

    private val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()

    val countries: List<AuthCountryUi> by lazy {
        phoneUtil.supportedRegions
            .filter { it.length == 2 && it != "ZZ" }
            .mapNotNull { iso2 ->
                val dialCode = phoneUtil.getCountryCodeForRegion(iso2)
                if (dialCode == 0) return@mapNotNull null

                val name = Locale("", iso2).getDisplayCountry(Locale.ENGLISH)
                if (name.isBlank()) return@mapNotNull null

                AuthCountryUi(
                    iso2 = iso2,
                    dialCode = "+$dialCode",
                    name = name,
                    flagEmoji = isoToFlagEmoji(iso2)
                )
            }
            .sortedBy { it.name }
    }

    val defaultCountry: AuthCountryUi
        get() = countries.firstOrNull { it.iso2 == "SY" }
            ?: countries.first()
}

private fun isoToFlagEmoji(iso2: String): String {
    if (iso2.length != 2) return "🏳️"
    val first = Character.codePointAt(iso2.uppercase(), 0) - 'A'.code + 0x1F1E6
    val second = Character.codePointAt(iso2.uppercase(), 1) - 'A'.code + 0x1F1E6
    return String(Character.toChars(first)) + String(Character.toChars(second))
}