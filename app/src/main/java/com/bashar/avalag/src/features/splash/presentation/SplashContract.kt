package com.bashar.avalag.src.features.splash.presentation

data class SplashState(var timer: Long, val isLoading: Boolean = true)

sealed class SplashEvents{
    data object NavigateToMainScreen : SplashEvents()
//    data object NavigateToHomeScreen : SplashEvents()
}