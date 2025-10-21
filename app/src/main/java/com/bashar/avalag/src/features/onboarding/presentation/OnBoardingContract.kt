package com.bashar.avalag.src.features.onboarding.presentation


data class OnBoardingState(var pages: List<OnboardingPage>)

sealed class OnBoardingEvents{
    data object NavigateToMainScreen : OnBoardingEvents()
//    data object NavigateToHomeScreen : SplashEvents()
}