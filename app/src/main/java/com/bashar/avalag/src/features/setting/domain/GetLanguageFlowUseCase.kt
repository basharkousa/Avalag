package com.bashar.avalag.src.features.setting.domain

import com.bashar.avalag.src.features.setting.domain.repositories.ISettingRepo


class GetLanguageFlowUseCase(private val repo: ISettingRepo) {
    operator fun invoke() = repo.observeLanguage()
}
