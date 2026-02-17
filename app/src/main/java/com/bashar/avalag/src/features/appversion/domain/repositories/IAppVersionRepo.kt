package com.bashar.avalag.src.features.appversion.domain.repositories

import com.bashar.avalag.src.features.appversion.domain.model.AppVersionInfo

interface IAppVersionRepo {
    suspend fun getAppVersionInfo(platform: String, version: String): AppVersionInfo
}
