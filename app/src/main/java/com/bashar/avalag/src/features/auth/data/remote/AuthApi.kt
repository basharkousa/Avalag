package com.bashar.avalag.src.features.auth.data.remote

import com.bashar.avalag.src.core.data.remote.model.ApiEnvelope
import com.bashar.avalag.src.features.auth.data.remote.dto.LoginDto
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApi {

    @Multipart
    @POST("login")
    suspend fun login(
        @Part("username_type") usernameType: RequestBody,
        @Part("username") username: RequestBody,
        @Part("key") key: RequestBody,
        @Part("password") password: RequestBody,
        @Part("fcm") fcm: RequestBody,
    ): ApiEnvelope<LoginDto>
}