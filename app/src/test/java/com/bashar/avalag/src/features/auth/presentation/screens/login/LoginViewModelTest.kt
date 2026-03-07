package com.bashar.avalag.src.features.auth.presentation.screens.login

import com.bashar.avalag.src.core.testing.MainDispatcherRule
import com.bashar.avalag.src.core.utils.UiText
import com.bashar.avalag.src.features.auth.domain.model.AuthSession
import com.bashar.avalag.src.features.auth.domain.model.AuthUser
import com.bashar.avalag.src.features.auth.domain.repositories.IAuthRepo
import com.bashar.avalag.src.features.auth.domain.usecases.LoginUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import com.bashar.avalag.R

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeAuthRepo : IAuthRepo {
        var calls = 0

        var result: Result<AuthSession> = Result.failure(
            IllegalStateException("not set")
        )

        override suspend fun login(
            username: String,
            key: String,
            password: String,
            fcm: String
        ): AuthSession {
            calls++
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
        assertNull(state.usernameError)
        assertNull(state.passwordError)
        assertNull(state.snackbarMessage)
        assertEquals(1, repo.calls)
    }

    @Test
    fun `empty username shows username error and does not call repo`() = runTest {
        val repo = FakeAuthRepo()
        val vm = LoginViewModel(LoginUseCase(repo))

        vm.onEvent(LoginEvents.UsernameChanged(""))
        vm.onEvent(LoginEvents.PasswordChanged("secret"))
        vm.onEvent(LoginEvents.LoginClicked)

        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNotNull(state.usernameError)
        assertEquals(state.usernameError, UiText.StringResource(R.string.phone_number_required))
        assertNull(state.navigateTo)
        assertEquals(0, repo.calls)
    }

    @Test
    fun `invalid username shows username error and does not call repo`() = runTest {
        val repo = FakeAuthRepo()
        val vm = LoginViewModel(LoginUseCase(repo))

        vm.onEvent(LoginEvents.UsernameChanged("23a15"))
        vm.onEvent(LoginEvents.PasswordChanged("secret"))
        vm.onEvent(LoginEvents.LoginClicked)

        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNotNull(state.usernameError)
        assertEquals(state.usernameError, UiText.StringResource(R.string.phone_number_invalid))
        assertNull(state.passwordError)
        assertNull(state.navigateTo)
        assertEquals(0, repo.calls)
    }

    @Test
    fun `short username shows username error and does not call repo`() = runTest {
        val repo = FakeAuthRepo()
        val vm = LoginViewModel(LoginUseCase(repo))

        vm.onEvent(LoginEvents.UsernameChanged("123"))
        vm.onEvent(LoginEvents.PasswordChanged("secret"))
        vm.onEvent(LoginEvents.LoginClicked)

        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNotNull(state.usernameError)
        assertNull(state.passwordError)
        assertNull(state.navigateTo)
        assertEquals(0, repo.calls)
    }

    @Test
    fun `empty password shows password error and does not call repo`() = runTest {
        val repo = FakeAuthRepo()
        val vm = LoginViewModel(LoginUseCase(repo))

        vm.onEvent(LoginEvents.UsernameChanged("23156544"))
        vm.onEvent(LoginEvents.PasswordChanged(""))
        vm.onEvent(LoginEvents.LoginClicked)

        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNull(state.usernameError)
        assertNotNull(state.passwordError)
        assertNull(state.navigateTo)
        assertEquals(0, repo.calls)
    }

    @Test
    fun `short password shows password error and does not call repo`() = runTest {
        val repo = FakeAuthRepo()
        val vm = LoginViewModel(LoginUseCase(repo))

        vm.onEvent(LoginEvents.UsernameChanged("23156544"))
        vm.onEvent(LoginEvents.PasswordChanged("123"))
        vm.onEvent(LoginEvents.LoginClicked)

        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNull(state.usernameError)
        assertNotNull(state.passwordError)
        assertNull(state.navigateTo)
        assertEquals(0, repo.calls)
    }

    @Test
    fun `both invalid fields show both errors and do not call repo`() = runTest {
        val repo = FakeAuthRepo()
        val vm = LoginViewModel(LoginUseCase(repo))

        vm.onEvent(LoginEvents.UsernameChanged(""))
        vm.onEvent(LoginEvents.PasswordChanged(""))
        vm.onEvent(LoginEvents.LoginClicked)

        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNotNull(state.usernameError)
        assertNotNull(state.passwordError)
        assertNull(state.navigateTo)
        assertEquals(0, repo.calls)
    }

    @Test
    fun `editing username clears username error`() = runTest {
        val repo = FakeAuthRepo()
        val vm = LoginViewModel(LoginUseCase(repo))

        vm.onEvent(LoginEvents.UsernameChanged(""))
        vm.onEvent(LoginEvents.PasswordChanged("secret"))
        vm.onEvent(LoginEvents.LoginClicked)

        advanceUntilIdle()
        assertNotNull(vm.state.value.usernameError)

        vm.onEvent(LoginEvents.UsernameChanged("23156544"))
        advanceUntilIdle()

        assertNull(vm.state.value.usernameError)
    }

    @Test
    fun `editing password clears password error`() = runTest {
        val repo = FakeAuthRepo()
        val vm = LoginViewModel(LoginUseCase(repo))

        vm.onEvent(LoginEvents.UsernameChanged("23156544"))
        vm.onEvent(LoginEvents.PasswordChanged(""))
        vm.onEvent(LoginEvents.LoginClicked)

        advanceUntilIdle()
        assertNotNull(vm.state.value.passwordError)

        vm.onEvent(LoginEvents.PasswordChanged("secret"))
        advanceUntilIdle()

        assertNull(vm.state.value.passwordError)
    }

    @Test
    fun `login error from repo shows snackbar after valid input`() = runTest {
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
        assertNull(state.usernameError)
        assertNull(state.passwordError)
        assertNull(state.navigateTo)
        assertNotNull(state.snackbarMessage)
        assertEquals(1, repo.calls)
    }
}