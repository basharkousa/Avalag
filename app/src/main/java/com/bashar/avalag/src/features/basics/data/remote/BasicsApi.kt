package com.bashar.avalag.src.features.basics.data.remote

import com.bashar.avalag.src.core.data.remote.model.ApiEnvelope
import com.bashar.avalag.src.features.basics.data.remote.dto.BasicsInfoDto
import com.bashar.avalag.src.features.basics.data.remote.dto.EnumsEnvelopeDto
import retrofit2.http.GET

interface BasicsApi {

    @GET("basics")
    suspend fun getBasicsInfo(): ApiEnvelope<BasicsInfoDto>

    @GET("enums")
    suspend fun getEnums(): ApiEnvelope<EnumsEnvelopeDto>

}

