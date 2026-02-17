package com.bashar.avalag.src.features.appversion.domain.usecase

import com.bashar.avalag.src.features.appversion.domain.model.AppVersionInfo
import com.bashar.avalag.src.features.appversion.domain.repositories.IAppVersionRepo

class GetAppVersionInfoUseCase(
    private val repo: IAppVersionRepo,
) {
    suspend operator fun invoke(platform: String, version: String): AppVersionInfo {
        return repo.getAppVersionInfo(platform, version)
    }
}
