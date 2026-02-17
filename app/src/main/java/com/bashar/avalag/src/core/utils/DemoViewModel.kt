package com.bashar.avalag.src.core.utils

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(
//    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _state by mutableStateOf(
        DemoState(
            name = ""
        )
    )
    val state: State<DemoState>
        get() = derivedStateOf { _state }

    fun onEvent(event: DemoEvents) {
//        when (event) {
//            TemplateEvents.NavigateToMainScreen -> {
//                // Navigation handled by the screen
//            }
//        }
    }

    init {
        println("ViewModelInitializing")
    }

//    private fun startTimerToNavigate() = viewModelScope.launch {
//            delay(3000)
//            navigateState.value = !navigateState.value
//            println("Navigate_to_Home ${navigateState.value}")
//        }

}
