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
import com.bashar.avalag.src.features.auth.domain.validation.AuthValidator
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
                _state = _state.copy(
                    username = event.value,
                    usernameError = null
                )

            }

            is LoginEvents.PasswordChanged -> {
                _state = _state.copy(
                    password = event.value,
                    passwordError = null)
            }

            is LoginEvents.CountryChanged -> {
                _state = _state.copy(selectedCountry = event.value)
            }

            LoginEvents.LoginClicked -> login()

            LoginEvents.ConsumeSnackbar -> {
                _state = _state.copy(snackbarMessage = null)
            }
        }
    }

    private fun login() {
        val username = _state.username.trim()
        val password = _state.password
        val key = _state.selectedCountry.dialCode

        val phoneError = AuthValidator.validatePhone(username)
        val passwordError = AuthValidator.validatePassword(password)

        if (phoneError != null || passwordError != null) {
            _state = _state.copy(
                usernameError = phoneError,
                passwordError = passwordError
            )
            return
        }


        viewModelScope.launch {
            _state = _state.copy(isLoading = true)

            val result = runCatching {
                loginUseCase(
                    username = username,
                    key = key,
                    password = password,
                    fcm = "11111111111111111111111111"
                )
            }.fold(
                onSuccess = { result->
                    _state = _state.copy(
                        isLoading = false,
                        navigateTo = LoginDestination.MAIN
                    )
                },
                onFailure = {error ->
                    _state = _state.copy(
                        isLoading = false,
                        snackbarMessage = NetworkErrorMapper.toUiText(error)
                    )
                    return@launch
                }
            )
/*
            result.fold(
                onSuccess = { result->
                    _state = _state.copy(
                        isLoading = false,
                        navigateTo = LoginDestination.MAIN
                    )
                },
                onFailure = {error ->
                    _state = _state.copy(
                        isLoading = false,
                        snackbarMessage = NetworkErrorMapper.toUiText(error)
                    )
                    return@launch
                }
            )

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
            )*/
        }
    }
}