package com.bashar.avalag.src.features.onboarding.presentation

import androidx.compose.runtime.getValue

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel


import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.bashar.avalag.src.core.ui.theme.WestMoscow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingViewModel = hiltViewModel(),
    onNavigateToMain: () -> Unit = {},
) {
    val state by viewModel.state
    ScreenContent(
        state = state,
        onEvent = { event ->
            when(event){
                is OnBoardingEvents.NavigateToMainScreen -> onNavigateToMain()
            }
        }
    )
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

//@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ScreenContent(
    state: OnBoardingState ,
    onEvent: (OnBoardingEvents) -> Unit = {}
) {
    // Handle navigation when loading completes
    LaunchedEffect(Unit) {

    }
    Scaffold() {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize().background(color = Color.Transparent),
//            color = Color.Transparent
        ) {
            OnboardingRoute(
                pages = state.pages,
                onFinish = {
                    onEvent(OnBoardingEvents.NavigateToMainScreen)
                })

        }
    }

}




data class OnboardingPage(
    @DrawableRes val imageRes: Int,
    val title: String,
    val subtitle: String
)

@Composable
fun OnboardingRoute(
    pages: List<OnboardingPage>,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            // subtle light gray background like your shots
            .background(Color(0xFFEDEDED))
    ) {

        // Full-bleed pager with the model photos
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = pages[page].imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Optional top/bottom gradient to help text contrast if needed
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0f to Color.Transparent,
                                0.7f to Color.Transparent,
                                1f to Color(0x33000000)
                            )
                        )
                )
            }
        }

        // Bottom card overlay
        Surface(
            color = Color(0xFF111214), // deep charcoal like your design
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 2.dp,
            shadowElevation = 8.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val page = pages[pagerState.currentPage]
                Text(
                    text = page.title,
                    style = TextStyle(
                        fontFamily = WestMoscow,
                        fontSize = 32.sp,
                        lineHeight = 38.4.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                    )
                )

                Text(
                    text = page.subtitle,
                    color = Color(0xCCFFFFFF),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DotsIndicator(
                        totalDots = pages.size,
                        selectedIndex = pagerState.currentPage,
                        activeWidth = 16.dp,
                        inactiveSize = 6.dp,
                        spacing = 8.dp,
                        activeColor = Color(0xFFE6E6E6),
                        inactiveColor = Color(0x66FFFFFF)
                    )

                    // Next / Done pill
                    val isLast = pagerState.currentPage == pages.lastIndex
                    Box(
                        modifier = Modifier
                            .size(width = 76.dp, height = 48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFFE6E6E6))
                            .noRippleClickable {
                                if (isLast) onFinish() else {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = if (isLast) "Finish" else "Next",
                            tint = Color(0xFF111214)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Simple indicator that matches your 2 small dots + 1 active pill look.
 */
@Composable
private fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    activeWidth: Dp,
    inactiveSize: Dp,
    spacing: Dp,
    activeColor: Color,
    inactiveColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .height(inactiveSize)
                    .animateContentSize()
                    .then(
                        if (isSelected)
                            Modifier
                                .width(activeWidth)
                                .clip(RoundedCornerShape(50))
                        else
                            Modifier
                                .size(inactiveSize)
                                .clip(CircleShape)
                    )
                    .background(if (isSelected) activeColor else inactiveColor)
            )
        }
    }
}

/**
 * Small convenience to avoid ripple on the arrow pill.
 */
@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit) = this.then(
    Modifier.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) { onClick() }
)


