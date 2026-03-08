package com.bashar.avalag.src.features.splash.presentation

import androidx.lifecycle.SavedStateHandle
import com.bashar.avalag.BuildConfig
import com.bashar.avalag.src.core.testing.MainDispatcherRule
import com.bashar.avalag.src.features.appversion.domain.model.AppVersionInfo
import com.bashar.avalag.src.features.appversion.domain.model.UpdateStatus
import com.bashar.avalag.src.features.appversion.domain.repositories.IAppVersionRepo
import com.bashar.avalag.src.features.appversion.domain.usecase.GetAppVersionInfoUseCase
import com.bashar.avalag.src.features.auth.domain.model.AuthSession
import com.bashar.avalag.src.features.auth.domain.repositories.IAuthLocalDataSource
import com.bashar.avalag.src.features.auth.domain.repositories.IAuthRepo
import com.bashar.avalag.src.features.auth.domain.usecases.GetTokenUseCase
import com.bashar.avalag.src.features.basics.domain.model.AppEnums
import com.bashar.avalag.src.features.basics.domain.model.BasicsInfo
import com.bashar.avalag.src.features.basics.domain.model.Country
import com.bashar.avalag.src.features.basics.domain.model.Promotion
import com.bashar.avalag.src.features.basics.domain.repositories.IBasicsRepo
import com.bashar.avalag.src.features.basics.domain.usecases.GetBasicsInfoUseCase
import com.bashar.avalag.src.features.basics.domain.usecases.GetEnumsUseCase
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

    private class FakeBasicsRepo : IBasicsRepo {
        var enumsResult: Result<AppEnums> = Result.failure(IllegalStateException("not set"))
        var basicsResult: Result<BasicsInfo> = Result.failure(IllegalStateException("not set"))

        var getEnumsCalls = 0
        var getBasicsCalls = 0

        override suspend fun getEnums(): AppEnums {
            getEnumsCalls++
            return enumsResult.getOrThrow()
        }

        override suspend fun getBasicsInfo(): BasicsInfo {
            getBasicsCalls++
            return basicsResult.getOrThrow()
        }
    }

    private class FakeAuthLocal : IAuthLocalDataSource {
        var token: String? = null

        override suspend fun saveToken(token: String) {
            this.token = token
        }

        override suspend fun getToken(): String? = token

        override suspend fun clearToken() {
            token = null
        }
    }

    private fun defaultEnums(): AppEnums =
        AppEnums(categories = mapOf("payment_methods" to mapOf("cash" to "Cash")))

    private fun defaultBasics(): BasicsInfo =
        BasicsInfo(
            promotions = listOf(Promotion(1, "t", "d", "img")),
            countries = listOf(Country(213, "Syria", "l.s"))
        )

    @Test
    fun `mandatory - shows update dialog and does not navigate and does not call bootstrap`() =
        runTest {
            val appRepo = FakeAppVersionRepo().apply {
                result = Result.success(
                    AppVersionInfo(
                        updateStatus = UpdateStatus.MANDATORY,
                        link = "https://www.apple.com/"
                    )
                )
            }
            val basicsRepo = FakeBasicsRepo().apply {
                enumsResult = Result.success(defaultEnums())
                basicsResult = Result.success(defaultBasics())
            }

            val authLocal = FakeAuthLocal().apply {
                token = "saved_token"
            }

            val vm = SplashViewModel(
                savedStateHandle = SavedStateHandle(),
                getAppVersionInfo = GetAppVersionInfoUseCase(appRepo),
                getBasicsInfo = GetBasicsInfoUseCase(basicsRepo),
                getEnums = GetEnumsUseCase(basicsRepo),
                getToken = GetTokenUseCase(authLocal)
            )

            advanceUntilIdle()

            val state = vm.state.value
            assertFalse(state.isLoading)
            assertNotNull(state.updateDialog)
            assertEquals("https://www.apple.com/", state.updateDialog?.link)
            assertNull(state.navigateTo)
            assertNull(state.snackbarMessage)

            // bootstrap NOT called
            assertEquals(0, basicsRepo.getEnumsCalls)
            assertEquals(0, basicsRepo.getBasicsCalls)

            // request payload basics
            assertEquals("android", appRepo.lastPlatform)
            assertFalse(appRepo.lastVersion.isNullOrBlank())
        }

    @Test
    fun `up_to_date - calls bootstrap and navigates to AUTH (stub isLoggedIn=false)`() = runTest {
        val appRepo = FakeAppVersionRepo().apply {
            result =
                Result.success(AppVersionInfo(updateStatus = UpdateStatus.UP_TO_DATE, link = null))
        }
        val basicsRepo = FakeBasicsRepo().apply {
            enumsResult = Result.success(defaultEnums())
            basicsResult = Result.success(defaultBasics())
        }

        val authLocal = FakeAuthLocal().apply {
            token = "saved_token"
        }

        val vm = SplashViewModel(
            savedStateHandle = SavedStateHandle(),
            getAppVersionInfo = GetAppVersionInfoUseCase(appRepo),
            getBasicsInfo = GetBasicsInfoUseCase(basicsRepo),
            getEnums = GetEnumsUseCase(basicsRepo),
            getToken = GetTokenUseCase(authLocal)
        )

        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNull(state.updateDialog)
        assertNull(state.snackbarMessage)
        assertEquals(SplashDestination.AUTH, state.navigateTo)

        // bootstrap CALLED
        assertEquals(1, basicsRepo.getEnumsCalls)
        assertEquals(1, basicsRepo.getBasicsCalls)

        assertEquals("android", appRepo.lastPlatform)
        assertEquals(BuildConfig.VERSION_NAME, appRepo.lastVersion)
    }

    @Test
    fun `up_to_date - bootstrap failure shows snackbar and does not navigate`() = runTest {
        val appRepo = FakeAppVersionRepo().apply {
            result =
                Result.success(AppVersionInfo(updateStatus = UpdateStatus.UP_TO_DATE, link = null))
        }
        val basicsRepo = FakeBasicsRepo().apply {
            enumsResult = Result.failure(RuntimeException("enums down"))
            basicsResult = Result.success(defaultBasics())
        }

        val authLocal = FakeAuthLocal().apply {
            token = "saved_token"
        }

        val vm = SplashViewModel(
            savedStateHandle = SavedStateHandle(),
            getAppVersionInfo = GetAppVersionInfoUseCase(appRepo),
            getBasicsInfo = GetBasicsInfoUseCase(basicsRepo),
            getEnums = GetEnumsUseCase(basicsRepo),
            getToken = GetTokenUseCase(authLocal)
        )

        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNull(state.updateDialog)
        assertNull(state.navigateTo)
        assertNotNull(state.snackbarMessage)

        // at least enums attempted
        assertEquals(1, basicsRepo.getEnumsCalls)
        // basics might still be attempted due to parallelism; we don't assert exact count here
    }


    @Test
    fun `up_to_date with token navigates to MAIN`() = runTest {
        val appRepo = FakeAppVersionRepo().apply {
            result =
                Result.success(AppVersionInfo(updateStatus = UpdateStatus.UP_TO_DATE, link = null))
        }
        val basicsRepo = FakeBasicsRepo().apply {
            enumsResult = Result.success(defaultEnums())
            basicsResult = Result.success(defaultBasics())
        }
        val authLocal = FakeAuthLocal().apply {
            token = "saved_token"
        }

        val vm = SplashViewModel(
            savedStateHandle = SavedStateHandle(),
            getAppVersionInfo = GetAppVersionInfoUseCase(appRepo),
            getBasicsInfo = GetBasicsInfoUseCase(basicsRepo),
            getEnums = GetEnumsUseCase(basicsRepo),
            getToken = GetTokenUseCase(authLocal)
        )

        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertEquals(SplashDestination.MAIN, state.navigateTo)
    }

    @Test
    fun `error - version check fails shows snackbar and does not call bootstrap`() = runTest {
        val appRepo = FakeAppVersionRepo().apply {
            result = Result.failure(RuntimeException("network down"))
        }
        val basicsRepo = FakeBasicsRepo().apply {
            enumsResult = Result.success(defaultEnums())
            basicsResult = Result.success(defaultBasics())
        }

        val authLocal = FakeAuthLocal().apply {
            token = "saved_token"
        }

        val vm = SplashViewModel(
            savedStateHandle = SavedStateHandle(),
            getAppVersionInfo = GetAppVersionInfoUseCase(appRepo),
            getBasicsInfo = GetBasicsInfoUseCase(basicsRepo),
            getEnums = GetEnumsUseCase(basicsRepo),
            getToken = GetTokenUseCase(authLocal)
        )

        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNull(state.updateDialog)
        assertNull(state.navigateTo)
        assertNotNull(state.snackbarMessage)

        // bootstrap NOT called
        assertEquals(0, basicsRepo.getEnumsCalls)
        assertEquals(0, basicsRepo.getBasicsCalls)
    }

    @Test
    fun `consume snackbar - clears snackbar message`() = runTest {
        val appRepo = FakeAppVersionRepo().apply {
            result = Result.failure(RuntimeException("network down"))
        }
        val basicsRepo = FakeBasicsRepo().apply {
            enumsResult = Result.success(defaultEnums())
            basicsResult = Result.success(defaultBasics())
        }

        val vm = SplashViewModel(
            savedStateHandle = SavedStateHandle(),
            getAppVersionInfo = GetAppVersionInfoUseCase(appRepo),
            getBasicsInfo = GetBasicsInfoUseCase(basicsRepo),
            getEnums = GetEnumsUseCase(basicsRepo),
            getToken = GetTokenUseCase(FakeAuthLocal())
        )

        advanceUntilIdle()
        assertNotNull(vm.state.value.snackbarMessage)

        vm.onEvent(SplashEvents.ConsumeSnackbar)
        advanceUntilIdle()

        assertNull(vm.state.value.snackbarMessage)
    }

    @Test
    fun `retry - after bootstrap failure succeeds and navigates`() = runTest {
        val appRepo = FakeAppVersionRepo().apply {
            result =
                Result.success(AppVersionInfo(updateStatus = UpdateStatus.UP_TO_DATE, link = null))
        }
        val basicsRepo = FakeBasicsRepo().apply {
            enumsResult = Result.failure(RuntimeException("enums down"))
            basicsResult = Result.success(defaultBasics())
        }

        val vm = SplashViewModel(
            savedStateHandle = SavedStateHandle(),
            getAppVersionInfo = GetAppVersionInfoUseCase(appRepo),
            getBasicsInfo = GetBasicsInfoUseCase(basicsRepo),
            getEnums = GetEnumsUseCase(basicsRepo),
            getToken = GetTokenUseCase(FakeAuthLocal())
        )

        advanceUntilIdle()
        assertNotNull(vm.state.value.snackbarMessage)
        assertNull(vm.state.value.navigateTo)

        // next attempt succeeds
        basicsRepo.enumsResult = Result.success(defaultEnums())

        vm.onEvent(SplashEvents.Retry)
        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNull(state.updateDialog)
        assertNull(state.snackbarMessage)
        assertEquals(SplashDestination.AUTH, state.navigateTo)
    }
}