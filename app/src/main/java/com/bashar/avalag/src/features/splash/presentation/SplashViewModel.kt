package com.bashar.avalag.src.features.splash.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _state by mutableStateOf(
        SplashState(
            timer = 4000L
        )
    )
    val state: State<SplashState>
        get() = derivedStateOf { _state }

    //todo I think here is not good !!!
    fun onEvent(event: SplashEvents) {
        when (event) {
            SplashEvents.NavigateToMainScreen -> {
                // Navigation handled by the screen
            }

//            SplashEvents.NavigateToHomeScreen -> TODO()
        }
    }

    private fun startCountdown() {
        viewModelScope.launch {
            delay(_state.timer)
//            val shouldNavigate = repository.checkSomeCondition()
            _state = _state.copy(
                isLoading = false,
            )
        }
    }

    init {
        println("SplashScreenInitializing")
        startCountdown()

        viewModelScope.launch {
            delay(_state.timer)


/*
            _screenState.emit(ScreenEvents.Navigate(Screen.ProjectsScreen.route))
*/
//            _screenState.emit(ScreenEvents.ShowSnackBar("Hi!! From viewModel!"))
        }

    }

//    private fun startTimerToNavigate() = viewModelScope.launch {
//            delay(3000)
//            navigateState.value = !navigateState.value
//            println("Navigate_to_Home ${navigateState.value}")
//        }


}


sealed class ScreenEvents{
    data class ShowSnackBar(val message: String): ScreenEvents()
    data class Navigate(val route: String): ScreenEvents()
}