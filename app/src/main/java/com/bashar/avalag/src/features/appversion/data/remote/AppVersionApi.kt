package com.bashar.avalag.src.features.appversion.data.remote

import com.bashar.avalag.src.core.data.remote.model.ApiEnvelope
import com.bashar.avalag.src.features.appversion.data.remote.dto.AppVersionDto
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

    interface AppVersionApi {

        @Multipart
        @POST("version")
        suspend fun checkAppVersion(
            @Part("platform") platform: RequestBody,
            @Part("version") version: RequestBody,
        ): ApiEnvelope<AppVersionDto>
    }
