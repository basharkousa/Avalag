package com.bashar.avalag.src.features.auth.data

import com.bashar.avalag.src.features.auth.data.remote.AuthApi
import com.bashar.avalag.src.features.auth.data.remote.mapper.toDomain
import com.bashar.avalag.src.features.auth.domain.model.AuthSession
import com.bashar.avalag.src.features.auth.domain.repositories.IAuthRepo
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val api: AuthApi,
    //Todo
//    private val local: IAuthLocalDataSource,
) : IAuthRepo {

    override suspend fun login(username: String, key: String, password: String, fcm: String): AuthSession {
        val text = "text/plain".toMediaType()

        val response = api.login(
            username = username.toRequestBody(text),
            key = key.toRequestBody(text),
            password = password.toRequestBody(text),
            fcm = fcm.toRequestBody(text),
            //Todo When Using web app
            usernameType = "phone".toRequestBody(text)
        )

        val dto = response.data ?: error("Missing data in login response")
        val token = dto.token ?: error("Missing token in login response")

        val session = dto.toDomain()

        //todo store token for splash/isLoggedIn later
//        local.saveToken(token)

        return session
    }

}