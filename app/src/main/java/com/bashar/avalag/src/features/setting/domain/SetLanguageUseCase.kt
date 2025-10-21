package com.bashar.avalag.src.features.setting.domain

import com.bashar.avalag.src.features.setting.domain.models.Language
import com.bashar.avalag.src.features.setting.domain.repositories.ISettingRepo


class SetLanguageUseCase(private val repo: ISettingRepo) {
    suspend operator fun invoke(language: Language) = repo.setLanguage(language)
}
