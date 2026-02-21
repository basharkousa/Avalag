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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAppVersionInfo: GetAppVersionInfoUseCase,
) : ViewModel() {

    private var _state by mutableStateOf(SplashState())
    val state: State<SplashState> get() = derivedStateOf { _state }

    init {
        checkVersion()
    }

    fun onEvent(event: SplashEvents) {
        when (event) {
            SplashEvents.Retry -> checkVersion()
            SplashEvents.ConsumeSnackbar -> _state = _state.copy(snackbarMessage = null)
        }
    }

    private fun checkVersion() {
        viewModelScope.launch {
            _state = SplashState(isLoading = true)

            val platform = "android"
            val version = BuildConfig.VERSION_NAME // e.g. "1.1.0"
//            val version = "2.0.0" // e.g. "1.1.0"

            val result = runCatching { getAppVersionInfo(platform, version) }

            val info = result.getOrNull()
            val error = result.exceptionOrNull()

            if (error != null) {
                _state = SplashState(
                    isLoading = false,
                    snackbarMessage = NetworkErrorMapper.toUiText(error),
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
                    // --- AUTH decision (stub now, real later) ---
                    val isLoggedIn = false // TODO: replace with token/session check
                    _state = SplashState(
                        isLoading = false,
                        navigateTo = if (isLoggedIn) SplashDestination.MAIN else SplashDestination.AUTH
                    )
                }
            }
        }
    }
}
