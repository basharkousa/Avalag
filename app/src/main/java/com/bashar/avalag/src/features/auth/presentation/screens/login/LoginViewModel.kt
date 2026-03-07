package com.bashar.avalag.src.features.auth.presentation.screens.login

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bashar.avalag.src.core.data.remote.errors.NetworkErrorMapper
import com.bashar.avalag.src.features.auth.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private var _state by mutableStateOf(LoginState())
    val state: State<LoginState> get() = derivedStateOf { _state }

    fun onEvent(event: LoginEvents) {
        when (event) {

            is LoginEvents.UsernameChanged -> {
                _state = _state.copy(username = event.value)
            }

            is LoginEvents.PasswordChanged -> {
                _state = _state.copy(password = event.value)
            }

            LoginEvents.LoginClicked -> login()

            LoginEvents.ConsumeSnackbar -> {
                _state = _state.copy(snackbarMessage = null)
            }
        }
    }

    private fun login() {

//        val username = _state.username
//        val password = _state.password
//        val key = _state.key
//
        val username = "23156544"
        val password = "secret"
        val key = "+963"

        viewModelScope.launch {

            _state = _state.copy(isLoading = true)

            val result = runCatching {
                loginUseCase(
                    username = username,
                    key = key,
                    password = password,
                    fcm = "11111111111111111111111111"
                )
            }

            val session = result.getOrNull()
            val error = result.exceptionOrNull()

            if (error != null) {
                _state = _state.copy(
                    isLoading = false,
                    snackbarMessage = NetworkErrorMapper.toUiText(error)
                )
                return@launch
            }

            _state = _state.copy(
                isLoading = false,
                navigateTo = LoginDestination.MAIN
            )
        }
    }
}