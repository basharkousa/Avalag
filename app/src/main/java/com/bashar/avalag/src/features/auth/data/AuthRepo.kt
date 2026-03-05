package com.bashar.avalag.src.features.auth.data

import com.bashar.avalag.src.features.auth.data.remote.AuthApi
import com.bashar.avalag.src.features.auth.domain.model.AccountStatus
import com.bashar.avalag.src.features.auth.domain.model.AuthSession
import com.bashar.avalag.src.features.auth.domain.model.AuthUser
import com.bashar.avalag.src.features.auth.domain.repositories.IAuthLocalDataSource
import com.bashar.avalag.src.features.auth.domain.repositories.IAuthRepo
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val api: AuthApi,
    private val local: IAuthLocalDataSource,
) : IAuthRepo {

    override suspend fun login(username: String, key: String, password: String, fcm: String): AuthSession {
        val text = "text/plain".toMediaType()

        val response = api.login(
            username = username.toRequestBody(text),
            key = key.toRequestBody(text),
            password = password.toRequestBody(text),
            fcm = fcm.toRequestBody(text),
        )

        val dto = response.data ?: error("Missing data in login response")
        val token = dto.token ?: error("Missing token in login response")
        val userDto = dto.user ?: error("Missing user in login response")

        val userId = userDto.id ?: error("Missing user.id in login response")

        val session = AuthSession(
            token = token,
            user = AuthUser(
                id = userId,
                qr = userDto.qr,
                name = userDto.name,
                key = userDto.key,
                phone = userDto.phone,
                image = userDto.image,
                email = userDto.email,
                accountStatus = userDto.accountStatus?.let {
                    AccountStatus(value = it.value, desc = it.desc)
                }
            )
        )

        // store token for splash/isLoggedIn later
        local.saveToken(token)

        return session
    }
}