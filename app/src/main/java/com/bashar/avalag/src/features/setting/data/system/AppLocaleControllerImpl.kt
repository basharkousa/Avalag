package com.bashar.avalag.src.features.setting.data.system


import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.bashar.avalag.src.features.setting.domain.models.Language
import com.bashar.avalag.src.features.setting.domain.services.AppLocaleController

class AppLocaleControllerImpl : AppLocaleController {
    override fun apply(language: Language) {
        val locales = if (language == Language.System)
            LocaleListCompat.getEmptyLocaleList()
        else
            LocaleListCompat.forLanguageTags(language.tag)
        AppCompatDelegate.setApplicationLocales(locales)
    }
}
