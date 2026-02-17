package com.bashar.avalag.src.features.splash.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.bashar.avalag.src.core.utils.UiText
import com.bashar.avalag.src.features.splash.presentation.test.SplashTestTags
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SplashContentUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loading_showsProgressIndicator() {
        composeRule.setContent {
            ScreenContent(
                state = SplashState(isLoading = true),
                onEvent = {}
            )
        }
        composeRule.onNodeWithTag(SplashTestTags.LOADING).assertIsDisplayed()
        composeRule.onNodeWithTag(SplashTestTags.UPDATE_DIALOG).assertDoesNotExist()
    }

    @Test
    fun mandatoryUpdate_clickUpdate_emitsOpenUpdateLink() {
        val events = mutableListOf<SplashUiEvent>()

        composeRule.setContent {
            ScreenContent(
                state = SplashState(
                    isLoading = false,
                    updateDialog = UpdateDialogState(link = "https://www.apple.com/"),
                    snackbarMessage = null,
                    navigateTo = null
                ),
                onEvent = { events.add(it) }
            )
        }

        composeRule.onNodeWithTag(SplashTestTags.UPDATE_DIALOG).assertIsDisplayed()
        composeRule.onNodeWithTag(SplashTestTags.UPDATE_BTN).performClick()

        composeRule.runOnIdle {
            assertTrue(events.contains(SplashUiEvent.OpenUpdateLink))
        }
    }

    @Test
    fun mandatoryUpdate_clickExit_emitsExitApp() {
        val events = mutableListOf<SplashUiEvent>()

        composeRule.setContent {
            ScreenContent(
                state = SplashState(
                    isLoading = false,
                    updateDialog = UpdateDialogState(link = "https://www.apple.com/"),
                    snackbarMessage = null,
                    navigateTo = null
                ),
                onEvent = { events.add(it) }
            )
        }

        composeRule.onNodeWithTag(SplashTestTags.EXIT_BTN).performClick()

        composeRule.runOnIdle {
            assertTrue(events.contains(SplashUiEvent.ExitApp))
        }
    }

    @Test
    fun error_snackbarRetry_click_emitsRetry() {
        val events = mutableListOf<SplashUiEvent>()

        composeRule.setContent {
            ScreenContent(
                state = SplashState(
                    isLoading = false,
                    updateDialog = null,
                    snackbarMessage = UiText.StringResource(0), // Random num
                    navigateTo = null
                ),
                onEvent = { events.add(it) }
            )
        }

        composeRule.onNodeWithTag(SplashTestTags.SNACKBAR_RETRY).assertIsDisplayed().performClick()

        composeRule.runOnIdle {
            assertTrue(events.contains(SplashUiEvent.Retry))
        }
    }
}
