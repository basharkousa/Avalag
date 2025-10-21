package com.bashar.avalag.src.features.setting.domain

import com.bashar.avalag.src.features.setting.domain.models.Language
import com.bashar.avalag.src.features.setting.domain.services.AppLocaleController


/** Triggers the platform side-effect of changing the app locales. */
class ApplyLanguageUseCase(private val controller: AppLocaleController) {
    operator fun invoke(language: Language) = controller.apply(language)
}
