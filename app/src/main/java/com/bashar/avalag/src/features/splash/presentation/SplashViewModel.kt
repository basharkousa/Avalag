package com.bashar.avalag.src.features.splash.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bashar.avalag.BuildConfig
import com.bashar.avalag.src.core.data.remote.errors.NetworkErrorMapper
import com.bashar.avalag.src.features.appversion.domain.model.UpdateStatus
import com.bashar.avalag.src.features.appversion.domain.usecase.GetAppVersionInfoUseCase
import com.bashar.avalag.src.features.auth.domain.usecases.GetTokenUseCase
import com.bashar.avalag.src.features.basics.domain.usecases.GetBasicsInfoUseCase
import com.bashar.avalag.src.features.basics.domain.usecases.GetEnumsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAppVersionInfo: GetAppVersionInfoUseCase,
    private val getBasicsInfo: GetBasicsInfoUseCase,
    private val getEnums: GetEnumsUseCase,
    private val getToken: GetTokenUseCase
) : ViewModel() {

    private var _state by mutableStateOf(SplashState())
    val state: State<SplashState> get() = derivedStateOf { _state }

    init {
        checkVersionAndBootstrap()
    }

    fun onEvent(event: SplashEvents) {
        when (event) {
            SplashEvents.Retry -> checkVersionAndBootstrap()
            SplashEvents.ConsumeSnackbar -> _state = _state.copy(snackbarMessage = null)
        }
    }

    private fun checkVersionAndBootstrap() {
        viewModelScope.launch {
            _state = SplashState(isLoading = true)

            val platform = "android"
//            val version = BuildConfig.VERSION_NAME
              val version = "2.0.0" // e.g. "1.1.0"

            val versionResult = runCatching { getAppVersionInfo(platform, version) }
            val info = versionResult.getOrNull()
            val versionError = versionResult.exceptionOrNull()

            if (versionError != null) {
                _state = SplashState(
                    isLoading = false,
                    snackbarMessage = NetworkErrorMapper.toUiText(versionError),
                    navigateTo = null
                )
                return@launch
            }

            when (info!!.updateStatus) {
                UpdateStatus.MANDATORY -> {
                    _state = SplashState(
                        isLoading = false,
                        updateDialog = UpdateDialogState(link = info.link),
                        navigateTo = null
                    )
                }

                UpdateStatus.UP_TO_DATE -> {
                    // Bootstrap preload (enums + basics). Fail => stay on splash with snackbar.
                    val bootstrapError = runCatching {
                        coroutineScope {
                            val enumsDeferred = async { getEnums() }
                            val basicsDeferred = async { getBasicsInfo() }

                            // We don't use the values yet, but forcing completion ensures they loaded.
                            enumsDeferred.await()
                            basicsDeferred.await()
                        }
                    }.exceptionOrNull()

                    if (bootstrapError != null) {
                        _state = SplashState(
                            isLoading = false,
                            snackbarMessage = NetworkErrorMapper.toUiText(bootstrapError),
                            navigateTo = null
                        )
                        return@launch
                    }

                    val isLoggedIn = !getToken().isNullOrBlank()
                    _state = SplashState(
                        isLoading = false,
                        navigateTo = if (isLoggedIn) SplashDestination.MAIN else SplashDestination.AUTH
                    )
                }
            }
        }
    }
}