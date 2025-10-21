package com.bashar.avalag.src.features.onboarding.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.bashar.avalag.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _state by mutableStateOf(
        OnBoardingState(
          pages = mutableListOf(OnboardingPage(
              imageRes = R.drawable.iv_slide1, // your first photo
              title = "Get Started with\nSmart Styling",
              subtitle = "Set up your fashion preferences for a personalized AI-powered experience."
          ),
              OnboardingPage(
                  imageRes = R.drawable.iv_slide2,
                  title = "Get Started with\nSmart Styling dnasd amnsd asnd amnsd amns ansmd a",
                  subtitle = "Set up your  asdmasndnas dasn,d asd asndasn dlkand lawndlkawndaknd landl andlakndlkawdfashion preferences for a personalized AI-powered experience."
              ),
              OnboardingPage(
                  imageRes = R.drawable.iv_slide3,
                  title = "Get d asdasd t Styling",
                  subtitle = "Set up your fashion preferences for a personalized AI-powered experience."
              ))
        )
    )
    val state: State<OnBoardingState>
        get() = derivedStateOf { _state }

    init {
        println("ViewModelInitializing")
    }

}