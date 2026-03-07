package com.bashar.avalag.src.features.auth.presentation.screens.login

import com.bashar.avalag.src.core.testing.MainDispatcherRule
import com.bashar.avalag.src.features.auth.domain.model.AuthSession
import com.bashar.avalag.src.features.auth.domain.model.AuthUser
import com.bashar.avalag.src.features.auth.domain.repositories.IAuthRepo
import com.bashar.avalag.src.features.auth.domain.usecases.LoginUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeAuthRepo : IAuthRepo {

        var result: Result<AuthSession> = Result.failure(IllegalStateException("not set"))

        override suspend fun login(
            username: String,
            key: String,
            password: String,
            fcm: String
        ): AuthSession {
            return result.getOrThrow()
        }
    }

    @Test
    fun `login success navigates to MAIN`() = runTest {

        val repo = FakeAuthRepo().apply {
            result = Result.success(
                AuthSession(
                    token = "token",
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

        val vm = LoginViewModel(LoginUseCase(repo))

        vm.onEvent(LoginEvents.UsernameChanged("23156544"))
        vm.onEvent(LoginEvents.PasswordChanged("secret"))

        vm.onEvent(LoginEvents.LoginClicked)

        advanceUntilIdle()

        val state = vm.state.value

        assertFalse(state.isLoading)
        assertEquals(LoginDestination.MAIN, state.navigateTo)
        assertNull(state.snackbarMessage)
    }

    @Test
    fun `login error shows snackbar`() = runTest {

        val repo = FakeAuthRepo().apply {
            result = Result.failure(RuntimeException("network error"))
        }

        val vm = LoginViewModel(LoginUseCase(repo))

        vm.onEvent(LoginEvents.UsernameChanged("23156544"))
        vm.onEvent(LoginEvents.PasswordChanged("secret"))

        vm.onEvent(LoginEvents.LoginClicked)

        advanceUntilIdle()

        val state = vm.state.value

        assertFalse(state.isLoading)
        assertNull(state.navigateTo)
        assertNotNull(state.snackbarMessage)
    }
}