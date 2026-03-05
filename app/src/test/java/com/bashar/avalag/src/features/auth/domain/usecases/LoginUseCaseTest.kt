package com.bashar.avalag.src.features.auth.domain.usecases

import com.bashar.avalag.src.features.auth.domain.model.AuthSession
import com.bashar.avalag.src.features.auth.domain.model.AuthUser
import com.bashar.avalag.src.features.auth.domain.repositories.IAuthRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class LoginUseCaseTest {

    private class FakeAuthRepo : IAuthRepo {
        var calls = 0
        var lastUsername: String? = null
        var lastKey: String? = null
        var lastPassword: String? = null
        var lastFcm: String? = null

        var result: Result<AuthSession> = Result.failure(IllegalStateException("not set"))

        override suspend fun login(username: String, key: String, password: String, fcm: String): AuthSession {
            calls++
            lastUsername = username
            lastKey = key
            lastPassword = password
            lastFcm = fcm
            return result.getOrThrow()
        }
    }

    @Test
    fun `usecase calls repo once and returns session`() = runBlocking {
        val repo = FakeAuthRepo().apply {
            result = Result.success(
                AuthSession(
                    token = "t",
                    user = AuthUser(
                        id = 9,
                        qr = null,
                        name = "qatar",
                        key = "+963",
                        phone = "23156544",
                        image = null,
                        email = null,
                        accountStatus = null
                    )
                )
            )
        }

        val useCase = LoginUseCase(repo)

        val session = useCase("23156544", "+963", "secret", "111")

        assertEquals(1, repo.calls)
        assertEquals("23156544", repo.lastUsername)
        assertEquals("+963", repo.lastKey)
        assertEquals("secret", repo.lastPassword)
        assertEquals("111", repo.lastFcm)

        assertEquals("t", session.token)
        assertEquals(9, session.user.id)
    }

    @Test
    fun `usecase propagates repo exception`() = runBlocking {
        val repo = FakeAuthRepo().apply {
            result = Result.failure(RuntimeException("network down"))
        }
        val useCase = LoginUseCase(repo)

        val ex = runCatching { useCase("u", "k", "p", "f") }.exceptionOrNull()

        assertNotNull(ex)
        assertEquals("network down", ex?.message)
        assertEquals(1, repo.calls)
    }
}