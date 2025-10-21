package com.bashar.avalag.src.features.setting.domain.services

import com.bashar.avalag.src.features.setting.domain.models.Language

/** Platform port to apply the chosen app language. */
interface AppLocaleController {
    fun apply(language: Language)
}
