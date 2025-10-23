package com.bashar.avalag.src.features.splash.presentation


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bashar.avalag.R
import com.bashar.avalag.src.core.ui.widgets.BackGroundThemeWidget
import com.bashar.avalag.src.core.ui.widgets.Greeting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToScreen: () -> Unit = {},
) {

    val state by viewModel.state
    SplashContent(
        state = state,
        onEvent = { event ->
            when(event) {
                is SplashEvents.NavigateToMainScreen -> onNavigateToScreen()
                else -> viewModel.onEvent(event)
            }
        }
    )

//    val navigateToHomeOnce = rememberSaveable {
//        mutableStateOf(true)
//    }

    LaunchedEffect(Unit) {
        println("Navigate_to_Home")
        /*  viewModel.screenState.collect { event ->
              when (event) {
                  is ScreenEvents.Navigate -> {
                      navController.navigate(Screen.ProjectsScreen.route) {
                          launchSingleTop = false
                          popUpTo(Screen.SplashScreen.route) {
                              inclusive = true
                          }
                      }
                  }

                  is ScreenEvents.ShowSnackBar -> {

                  }
              }
          }*/

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SplashContent(
    state: SplashState = SplashState(isLoading = true, timer = 1000),
    onEvent: (SplashEvents) -> Unit = {}
) {
    // Handle navigation when loading completes
    LaunchedEffect(state.isLoading){
        if (!state.isLoading) {
            onEvent(SplashEvents.NavigateToMainScreen)
        }
    }
    Scaffold() {
        BackGroundThemeWidget(Modifier.fillMaxSize())
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize().background(color = Color.Transparent),
//            color = Color.Transparent
        ) {

            Image(
                painterResource(R.drawable.ic_logo),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier.align (Alignment.Center)
                    .width(96.33098.dp)
                    .height(93.42924.dp)
            )

            Image(
                painterResource(R.drawable.ic_avalag_logo),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier.align (Alignment.BottomCenter).padding(41.51.dp)
                    .width(197.00002.dp)
                    .height(58.48578.dp)
            )
        }
    }

}

