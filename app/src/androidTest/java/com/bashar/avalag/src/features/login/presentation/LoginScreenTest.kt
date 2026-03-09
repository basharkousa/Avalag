package com.bashar.avalag.src.features.login.presentation

import androidx.activity.ComponentActivity
import com.bashar.avalag.src.core.utils.UiText
import com.bashar.avalag.src.features.auth.presentation.data.AuthCountriesProvider
import com.bashar.avalag.src.features.auth.presentation.screens.login.LoginState
import com.bashar.avalag.src.features.auth.presentation.screens.login.LoginUiEvent
import com.bashar.avalag.src.features.auth.presentation.screens.login.ScreenContent
import com.bashar.avalag.src.features.auth.presentation.screens.login.test.LoginTestTags
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.bashar.avalag.src.features.auth.presentation.screens.login.LoginDestination

class LoginScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun login_content_shows_basic_ui() {
        composeRule.setContent {
            ScreenContent(
                state = LoginState(
                    selectedCountry = AuthCountriesProvider.defaultCountry,
                    availableCountries = AuthCountriesProvider.countries
                )
            )
        }

        composeRule.onNodeWithTag(LoginTestTags.USERNAME_FIELD).assertIsDisplayed()
        composeRule.onNodeWithTag(LoginTestTags.PASSWORD_FIELD).assertIsDisplayed()
        composeRule.onNodeWithTag(LoginTestTags.LOGIN_BUTTON).assertIsDisplayed()
        composeRule.onNodeWithTag(LoginTestTags.SIGNUP_BUTTON).assertIsDisplayed()
        composeRule.onNodeWithTag(LoginTestTags.RESET_PASSWORD_BUTTON).assertIsDisplayed()
    }

    @Test
    fun login_click_emits_event() {
        var clicked = false

        composeRule.setContent {
            ScreenContent(
                state = LoginState(
                    selectedCountry = AuthCountriesProvider.defaultCountry,
                    availableCountries = AuthCountriesProvider.countries
                ),
                onEvent = { event ->
                    if (event == LoginUiEvent.LoginClicked) {
                        clicked = true
                    }
                }
            )
        }

        composeRule.onNodeWithTag(LoginTestTags.LOGIN_BUTTON).performClick()

        composeRule.runOnIdle {
            Assert.assertTrue(clicked)
        }
    }

    @Test
    fun signup_click_emits_event() {
        var clicked = false

        composeRule.setContent {
            ScreenContent(
                state = LoginState(
                    selectedCountry = AuthCountriesProvider.defaultCountry,
                    availableCountries = AuthCountriesProvider.countries
                ),
                onEvent = { event ->
                    if (event == LoginUiEvent.Navigate(LoginDestination.SIGNUP)) {
                        clicked = true
                    }
                }
            )
        }

        composeRule.onNodeWithTag(LoginTestTags.SIGNUP_BUTTON).performClick()

        composeRule.runOnIdle {
            Assert.assertTrue(clicked)
        }
    }

    @Test
    fun reset_password_click_emits_event() {
        var clicked = false

        composeRule.setContent {
            ScreenContent(
                state = LoginState(
                    selectedCountry = AuthCountriesProvider.defaultCountry,
                    availableCountries = AuthCountriesProvider.countries
                ),
                onEvent = { event ->
                    if (event == LoginUiEvent.Navigate(LoginDestination.RESET_PASSWORD)) {
                        clicked = true
                    }
                }
            )
        }

        composeRule.onNodeWithTag(LoginTestTags.RESET_PASSWORD_BUTTON).performClick()

        composeRule.runOnIdle {
            Assert.assertTrue(clicked)
        }
    }

    @Test
    fun typing_username_emits_event() {
        var latestUsername = ""

        composeRule.setContent {
            ScreenContent(
                state = LoginState(
                    selectedCountry = AuthCountriesProvider.defaultCountry,
                    availableCountries = AuthCountriesProvider.countries
                ),
                onEvent = { event ->
                    if (event is LoginUiEvent.UsernameChanged) {
                        latestUsername = event.value
                    }
                }
            )
        }

        composeRule.onNodeWithTag(LoginTestTags.USERNAME_FIELD,useUnmergedTree = true)
            .performClick()
            .performTextInput("23156544")

        composeRule.runOnIdle {
            Assert.assertEquals("23156544", latestUsername)
        }
    }

    @Test
    fun typing_password_emits_event() {
        var latestPassword = ""

        composeRule.setContent {
            ScreenContent(
                state = LoginState(
                    selectedCountry = AuthCountriesProvider.defaultCountry,
                    availableCountries = AuthCountriesProvider.countries
                ),
                onEvent = { event ->
                    if (event is LoginUiEvent.PasswordChanged) {
                        latestPassword = event.value
                    }
                }
            )
        }

        composeRule.onNodeWithTag(LoginTestTags.PASSWORD_FIELD,useUnmergedTree = true)
            .performClick()
            .performTextInput("secret")

        composeRule.runOnIdle {
            Assert.assertEquals("secret", latestPassword)
        }
    }

    @Test
    fun loading_indicator_shows_when_loading() {
        composeRule.setContent {
            ScreenContent(
                state = LoginState(
                    isLoading = true,
                    selectedCountry = AuthCountriesProvider.defaultCountry,
                    availableCountries = AuthCountriesProvider.countries
                )
            )
        }
        composeRule.onNodeWithTag(LoginTestTags.LOGIN_BUTTON,useUnmergedTree = true).assertIsNotEnabled()
        composeRule.onNodeWithTag(LoginTestTags.LOADING).assertIsDisplayed()
    }

    @Test
    fun loading_indicator_hidden_when_not_loading() {
        composeRule.setContent {
            ScreenContent(
                state = LoginState(
                    isLoading = false,
                    selectedCountry = AuthCountriesProvider.defaultCountry,
                    availableCountries = AuthCountriesProvider.countries
                )
            )
        }

        composeRule.onNodeWithTag(LoginTestTags.LOADING).assertDoesNotExist()
    }

    @Test
    fun username_error_is_displayed() {
        composeRule.setContent {
            ScreenContent(
                state = LoginState(
                    usernameError = UiText.DynamicString("Phone number is required"),
                    selectedCountry = AuthCountriesProvider.defaultCountry,
                    availableCountries = AuthCountriesProvider.countries
                )
            )
        }

        composeRule.onNodeWithText("Phone number is required").assertIsDisplayed()
    }

    @Test
    fun password_error_is_displayed() {
        composeRule.setContent {
            ScreenContent(
                state = LoginState(
                    passwordError = UiText.DynamicString("Password is required"),
                    selectedCountry = AuthCountriesProvider.defaultCountry,
                    availableCountries = AuthCountriesProvider.countries
                )
            )
        }

        composeRule.onNodeWithText("Password is required").assertIsDisplayed()
    }

    @Test
    fun snackBar_shows_and_retry_emits_login_click() {
        var retried = false

        composeRule.setContent {
            ScreenContent(
                state = LoginState(
                    snackbarMessage = UiText.DynamicString("Network error"),
                    selectedCountry = AuthCountriesProvider.defaultCountry,
                    availableCountries = AuthCountriesProvider.countries
                ),
                onEvent = { event ->
                    if (event == LoginUiEvent.LoginClicked) {
                        retried = true
                    }
                }
            )
        }

        composeRule.onNodeWithTag(LoginTestTags.SNACKBAR_RETRY).assertIsDisplayed()
        composeRule.onNodeWithTag(LoginTestTags.SNACKBAR_RETRY).performClick()

        composeRule.runOnIdle {
            Assert.assertTrue(retried)
        }
    }
}