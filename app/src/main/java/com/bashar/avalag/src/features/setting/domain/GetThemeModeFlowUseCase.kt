package com.bashar.avalag.src.features.setting.domain


import com.bashar.avalag.src.features.setting.domain.repositories.ISettingRepo


class GetThemeModeFlowUseCase(private val repo: ISettingRepo) {
    operator fun invoke() = repo.observeTheme()
}
