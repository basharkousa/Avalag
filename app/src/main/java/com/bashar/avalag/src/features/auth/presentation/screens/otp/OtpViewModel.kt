package com.bashar.avalag.src.features.auth.presentation.screens.otp

import androidx.lifecycle.ViewModel
import com.bashar.avalag.src.features.auth.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class OtpViewModel(
    private val repo: AuthRepository,
) : ViewModel() {


    private val _state = MutableStateFlow(OtpState())
    val state: StateFlow<OtpState> = _state.asStateFlow()


    fun onEvent(event: OtpEvent) {
//        when (event) {
//            is LoginEvent.EmailChanged -> _state.value = _state.value.copy(email = event.value)
//            is LoginEvent.PasswordChanged -> _state.value = _state.value.copy(password = event.value)
//            LoginEvent.TogglePasswordVisibility -> _state.value = _state.value.copy(
//                isPasswordVisible = !_state.value.isPasswordVisible
//            )
//            LoginEvent.Submit -> submit()
//            LoginEvent.ClearError -> _state.value = _state.value.copy(errorMessage = null)
//        }
    }


    private fun submit() {
        val s = _state.value
        if (!s.canSubmit) return
      /*  viewModelScope.launch {
            _state.value = s.copy(isLoading = true, errorMessage = null)
            val result = repo.login(s.email.trim(), s.password)
            result.onSuccess { token ->
                _effects.send(LoginEffect.NavigateHome(token))
            }.onFailure { th ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = th.message ?: "unknown_error"
                )
            }
// if success, keep loading until collector navigates away; otherwise we reset above
        }*/
    }
}