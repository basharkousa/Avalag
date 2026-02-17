package com.bashar.avalag.src.features.appversion.data

import com.bashar.avalag.src.features.appversion.data.remote.AppVersionApi
import com.bashar.avalag.src.features.appversion.domain.model.AppVersionInfo
import com.bashar.avalag.src.features.appversion.domain.model.UpdateStatus
import com.bashar.avalag.src.features.appversion.domain.repositories.IAppVersionRepo
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AppVersionRepo @Inject constructor(
    private val api: AppVersionApi,
) : IAppVersionRepo {

    override suspend fun getAppVersionInfo(platform: String, version: String): AppVersionInfo {
        val text = "text/plain".toMediaType()
        val response = api.checkAppVersion(
            platform = platform.toRequestBody(text),
            version = version.toRequestBody(text),
        )

        val dto = response.data ?: error("Missing data in version response")

        val status = UpdateStatus.fromApi(dto.updateStatus)
            ?: error("Unknown update_status: ${dto.updateStatus}")

        return AppVersionInfo(
            updateStatus = status,
            link = dto.link,
        )
    }
}
