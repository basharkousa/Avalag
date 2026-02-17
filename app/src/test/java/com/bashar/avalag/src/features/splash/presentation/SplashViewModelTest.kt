package com.bashar.avalag.src.features.splash.presentation

import androidx.lifecycle.SavedStateHandle
import com.bashar.avalag.BuildConfig
import com.bashar.avalag.src.core.testing.MainDispatcherRule
import com.bashar.avalag.src.features.appversion.domain.model.AppVersionInfo
import com.bashar.avalag.src.features.appversion.domain.model.UpdateStatus
import com.bashar.avalag.src.features.appversion.domain.repositories.IAppVersionRepo
import com.bashar.avalag.src.features.appversion.domain.usecase.GetAppVersionInfoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeAppVersionRepo : IAppVersionRepo {
        var result: Result<AppVersionInfo> = Result.failure(IllegalStateException("not set"))
        var lastPlatform: String? = null
        var lastVersion: String? = null

        override suspend fun getAppVersionInfo(platform: String, version: String): AppVersionInfo {
            lastPlatform = platform
            lastVersion = version
            return result.getOrThrow()
        }
    }

    @Test
    fun `mandatory - shows update dialog and does not navigate`() = runTest {
        val repo = FakeAppVersionRepo().apply {
            result = Result.success(
                AppVersionInfo(
                    updateStatus = UpdateStatus.MANDATORY,
                    link = "https://www.apple.com/"
                )
            )
        }
        val vm = SplashViewModel(
            savedStateHandle = SavedStateHandle(),
            getAppVersionInfo = GetAppVersionInfoUseCase(repo)
        )

        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNotNull(state.updateDialog)
        assertEquals("https://www.apple.com/", state.updateDialog?.link)
        assertNull(state.navigateTo)
        assertNull(state.snackbarMessage)

        // request payload basics
        assertEquals("android", repo.lastPlatform)
        assertFalse(repo.lastVersion.isNullOrBlank()) // should be BuildConfig.VERSION_NAME
    }

    @Test
    fun `up_to_date - navigates to AUTH (stub isLoggedIn=false)`() = runTest {
        val repo = FakeAppVersionRepo().apply {
            result = Result.success(
                AppVersionInfo(
                    updateStatus = UpdateStatus.UP_TO_DATE,
                    link = null
                )
            )
        }
        val vm = SplashViewModel(
            savedStateHandle = SavedStateHandle(),
            getAppVersionInfo = GetAppVersionInfoUseCase(repo)
        )

        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNull(state.updateDialog)
        assertNull(state.snackbarMessage)
        assertEquals(SplashDestination.AUTH, state.navigateTo)

        assertEquals("android", repo.lastPlatform)
        assertEquals(BuildConfig.VERSION_NAME, repo.lastVersion)
    }

    @Test
    fun `error - shows snackbar and stays on splash`() = runTest {
        val repo = FakeAppVersionRepo().apply {
            result = Result.failure(RuntimeException("network down"))
        }
        val vm = SplashViewModel(
            savedStateHandle = SavedStateHandle(),
            getAppVersionInfo = GetAppVersionInfoUseCase(repo)
        )

        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNull(state.updateDialog)
        assertNull(state.navigateTo)
        assertNotNull(state.snackbarMessage)
    }

    @Test
    fun `consume snackbar - clears snackbar message`() = runTest {
        val repo = FakeAppVersionRepo().apply {
            result = Result.failure(RuntimeException("network down"))
        }
        val vm = SplashViewModel(
            savedStateHandle = SavedStateHandle(),
            getAppVersionInfo = GetAppVersionInfoUseCase(repo)
        )

        advanceUntilIdle()
        assertNotNull(vm.state.value.snackbarMessage)

        vm.onEvent(SplashEvents.ConsumeSnackbar)
        advanceUntilIdle()

        assertNull(vm.state.value.snackbarMessage)
    }

    @Test
    fun `retry - after error succeeds and navigates`() = runTest {
        val repo = FakeAppVersionRepo().apply {
            result = Result.failure(RuntimeException("network down"))
        }
        val vm = SplashViewModel(
            savedStateHandle = SavedStateHandle(),
            getAppVersionInfo = GetAppVersionInfoUseCase(repo)
        )

        advanceUntilIdle()
        assertNotNull(vm.state.value.snackbarMessage)
        assertNull(vm.state.value.navigateTo)

        // next attempt succeeds
        repo.result = Result.success(
            AppVersionInfo(updateStatus = UpdateStatus.UP_TO_DATE, link = null)
        )

        vm.onEvent(SplashEvents.Retry)
        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNull(state.updateDialog)
        assertNull(state.snackbarMessage)
        assertEquals(SplashDestination.AUTH, state.navigateTo)
    }
}
