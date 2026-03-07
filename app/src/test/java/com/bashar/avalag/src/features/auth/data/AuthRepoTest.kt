package com.bashar.avalag.src.features.auth.data

import com.bashar.avalag.src.core.data.remote.model.ApiEnvelope
import com.bashar.avalag.src.features.auth.data.remote.AuthApi
import com.bashar.avalag.src.features.auth.data.remote.dto.AccountStatusDto
import com.bashar.avalag.src.features.auth.data.remote.dto.LoginDto
import com.bashar.avalag.src.features.auth.data.remote.dto.LoginUserDto
import com.bashar.avalag.src.features.auth.domain.repositories.IAuthLocalDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.RequestBody
import okio.Buffer
import org.junit.Assert.*
import org.junit.Test

class AuthRepoTest {
    private class FakeAuthApi(
        private val response: ApiEnvelope<LoginDto>
    ) : AuthApi {

        private fun RequestBody.asUtf8(): String {
            val buffer = Buffer()
            writeTo(buffer)
            return buffer.readUtf8()
        }

        var lastUsername: String? = null
        var lastKey: String? = null
        var lastPassword: String? = null
        var lastFcm: String? = null

        override suspend fun login(
            usernameType : RequestBody,
            username: RequestBody,
            key: RequestBody,
            password: RequestBody,
            fcm: RequestBody,

        ): ApiEnvelope<LoginDto> {
            lastUsername = username.asUtf8()
            lastKey = key.asUtf8()
            lastPassword = password.asUtf8()
            lastFcm = fcm.asUtf8()
            return response
        }
    }

    private class FakeAuthLocal : IAuthLocalDataSource {
        var savedToken: String? = null
        var saveCalls = 0

        override suspend fun saveToken(token: String) {
            saveCalls++
            savedToken = token
        }

        override suspend fun getToken(): String? = savedToken

        override suspend fun clearToken() {
            savedToken = null
        }
    }

    @Test
    fun `repo maps login response, passes payload, and saves token`() = runBlocking {
        val api = FakeAuthApi(
            ApiEnvelope(
                status = "success",
                message = "Verified successfully",
                data = LoginDto(
                    token = "token_123",
                    user = LoginUserDto(
                        id = 9,
                        qr = "78979845",
                        name = "qatar",
                        key = "+963",
                        phone = "23156544",
                        image = "https://avalag.com/uploads/default/default.jpg",
                        email = "customer@sy.com",
                        accountStatus = AccountStatusDto(value = "active", desc = "Active")
                    )
                )
            )
        )
        val local = FakeAuthLocal()
        val repo = AuthRepo(api,)

        val session = repo.login(
            username = "23156544",
            key = "+963",
            password = "secret",
            fcm = "11111111111111111111111111"
        )

        // payload
        assertEquals("23156544", api.lastUsername)
        assertEquals("+963", api.lastKey)
        assertEquals("secret", api.lastPassword)
        assertEquals("11111111111111111111111111", api.lastFcm)

        // mapping
        assertEquals("token_123", session.token)
        assertEquals(9, session.user.id)
        assertEquals("qatar", session.user.name)
        assertEquals("active", session.user.accountStatus?.value)
        assertEquals("Active", session.user.accountStatus?.desc)

        // token saved
        assertEquals(1, local.saveCalls)
        assertEquals("token_123", local.savedToken)
    }

    @Test
    fun `repo throws when data missing`() = runBlocking {
        val api = FakeAuthApi(
            ApiEnvelope(
                status = "success",
                message = "Verified successfully",
                data = null
            )
        )
        val repo = AuthRepo(api,)

        val ex = runCatching {
            repo.login("23156544", "+963", "secret", "111")
        }.exceptionOrNull()

        assertNotNull(ex)
        assertTrue(ex!!.message?.contains("Missing data") == true)
    }

    @Test
    fun `repo throws when token missing`() = runBlocking {
        val api = FakeAuthApi(
            ApiEnvelope(
                status = "success",
                message = "Verified successfully",
                data = LoginDto(
                    token = null,
                    user = LoginUserDto(id = 9)
                )
            )
        )
        val repo = AuthRepo(api,)

        val ex = runCatching {
            repo.login("23156544", "+963", "secret", "111")
        }.exceptionOrNull()

        assertNotNull(ex)
        assertTrue(ex!!.message?.contains("Missing token") == true)
    }

    @Test
    fun `repo throws when user missing`() = runBlocking {
        val api = FakeAuthApi(
            ApiEnvelope(
                status = "success",
                message = "Verified successfully",
                data = LoginDto(
                    token = "token_123",
                    user = null
                )
            )
        )
        val repo = AuthRepo(api,)

        val ex = runCatching {
            repo.login("23156544", "+963", "secret", "111")
        }.exceptionOrNull()

        assertNotNull(ex)
        assertTrue(ex!!.message?.contains("Missing user") == true)
    }

    @Test
    fun `repo throws when user_id missing`() = runBlocking {
        val api = FakeAuthApi(
            ApiEnvelope(
                status = "success",
                message = "Verified successfully",
                data = LoginDto(
                    token = "token_123",
                    user = LoginUserDto(id = null)
                )
            )
        )
        val repo = AuthRepo(api, )

        val ex = runCatching {
            repo.login("23156544", "+963", "secret", "111")
        }.exceptionOrNull()

        assertNotNull(ex)
        assertTrue(ex!!.message?.contains("Missing user.id") == true)
    }
}