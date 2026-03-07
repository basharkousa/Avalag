package com.bashar.avalag.src.features.auth.presentation.screens.login


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bashar.avalag.R
import com.bashar.avalag.src.core.ui.theme.Primary100
import com.bashar.avalag.src.core.ui.widgets.DefaultButton
import com.bashar.avalag.src.core.ui.widgets.DefaultTextField
import com.bashar.avalag.src.core.utils.AvalagMultiPreview
import com.bashar.avalag.src.core.utils.ScreenTemplate
import com.bashar.avalag.src.core.utils.UiText
import com.bashar.avalag.src.core.utils.asString
import com.bashar.avalag.src.features.auth.presentation.mapper.toAuthModel
import com.bashar.avalag.src.features.auth.presentation.mapper.toWidgetModel
import com.bashar.avalag.src.features.auth.presentation.screens.login.test.LoginTestTags

import com.bashar.avalag.src.features.auth.presentation.widgets.AuthTppBar
import com.bashar.avalag.src.features.auth.presentation.widgets.OrDivider


@Composable
fun LoginScreen(
    onNavigateToMain: () -> Unit,
    onNavigateToSignup: () -> Unit,
    onNavigateToResetPassword: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    vm: LoginViewModel = hiltViewModel(),
) {
    val state by vm.state

    ScreenContent(
        state = state,
        onEvent = { event ->
            when (event) {
                is LoginUiEvent.UsernameChanged ->
                    vm.onEvent(LoginEvents.UsernameChanged(event.value))

                is LoginUiEvent.PasswordChanged ->
                    vm.onEvent(LoginEvents.PasswordChanged(event.value))

                is LoginUiEvent.CountryChanged ->
                    vm.onEvent(LoginEvents.CountryChanged(event.value))


                LoginUiEvent.LoginClicked ->
                    vm.onEvent(LoginEvents.LoginClicked)

                LoginUiEvent.ConsumeSnackbar ->
                    vm.onEvent(LoginEvents.ConsumeSnackbar)

                LoginUiEvent.BackClicked -> onBack()

                LoginUiEvent.SkipClicked -> onSkip()

                is LoginUiEvent.Navigate -> {
                    when (event.to) {
                        LoginDestination.MAIN -> onNavigateToMain()
                        LoginDestination.SIGNUP -> onNavigateToSignup()
                        LoginDestination.RESET_PASSWORD -> onNavigateToResetPassword()
                        LoginDestination.BACK -> onBack()
                        LoginDestination.SKIP -> onSkip()
                    }
                }
            }
        }
    )
}


@Composable
internal fun ScreenContent(
    state: LoginState = LoginState(),
    onEvent: (LoginUiEvent) -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(state.navigateTo) {
        state.navigateTo?.let { onEvent(LoginUiEvent.Navigate(it)) }
    }

    LaunchedEffect(state.snackbarMessage) {
        val uiText = state.snackbarMessage ?: return@LaunchedEffect
        val msg = uiText.asString(context)
        val retryLabel = UiText.StringResource(R.string.retry).asString(context)

        val result = snackbarHostState.showSnackbar(
            message = msg,
            actionLabel = retryLabel,
            duration = SnackbarDuration.Short
        )

        onEvent(LoginUiEvent.ConsumeSnackbar)

        if (result == SnackbarResult.ActionPerformed) {
            onEvent(LoginUiEvent.LoginClicked)
        }
    }

    ScreenTemplate(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    action = {
                        val label = data.visuals.actionLabel
                        if (!label.isNullOrBlank()) {
                            TextButton(
                                modifier = Modifier.testTag(LoginTestTags.SNACKBAR_RETRY),
                                onClick = { data.performAction() }
                            ) {
                                Text(label, color = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }
                ) {
                    Text(data.visuals.message)
                }
            }
        },
    ) { padding ->
        LoginBody(
            state = state,
            onEvent = onEvent
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@AvalagMultiPreview
@Composable
private fun LoginBody(
    state: LoginState = LoginState(),
    onEvent: (LoginUiEvent) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Scaffold(
            topBar = {
                AuthTppBar(stringResource = R.string.login, onBack = {
                    onEvent(LoginUiEvent.BackClicked)
                }, canSkip = true, onSkip = {
                    onEvent(LoginUiEvent.SkipClicked)
                })
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.login_title),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Primary100,
                            fontWeight = FontWeight.W500
                        )
                    )

                    Spacer(Modifier.height(40.dp))

                    // Phone row (country + phone)
                    DefaultTextField(
                        isPhone = true,
                        country = state.selectedCountry.toWidgetModel(),
                        countries = state.availableCountries.map { it.toWidgetModel() },
                        onCountryChange = { selected ->
                            val authCountry = state.availableCountries.firstOrNull {
                                it.dialCode == selected.code && it.name == selected.name
                            } ?: return@DefaultTextField

                            onEvent(LoginUiEvent.CountryChanged(authCountry))
                        },
                        value = state.username,
                        onValueChange = { onEvent(LoginUiEvent.UsernameChanged(it)) },
                        placeholder = stringResource(R.string.phone_number)
                    )

                    Spacer(Modifier.height(12.dp))

                    // Password
                    DefaultTextField(
                        value = state.password,
                        onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
                        placeholder = stringResource(R.string.password),
                        isPassword = true
                    )

                    Spacer(Modifier.height(14.dp))

                    // Links row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.create_new_account),
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W500),
                            modifier = Modifier.clickable {
                                onEvent(LoginUiEvent.Navigate(LoginDestination.SIGNUP))
                            }
                        )
                        Text(
                            text = stringResource(R.string.forgot_password),
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W500),
                            modifier = Modifier.clickable {
                                onEvent(LoginUiEvent.Navigate(LoginDestination.RESET_PASSWORD))
                            }
                        )
                    }

                    Spacer(Modifier.height(40.dp))

                    // Primary button
                    DefaultButton(
                        text = stringResource(R.string.next),
                        onClick = { onEvent(LoginUiEvent.LoginClicked) }
                    )

                    Spacer(Modifier.height(20.dp))

                    // Divider with "Or"
                    OrDivider(label = stringResource(R.string.or))

                    Spacer(Modifier.height(20.dp))

                    // Secondary actions
                    DefaultButton(
                        text = stringResource(R.string.continue_with_email),
                        isOutlined = true,
                        onClick = {

                        }
                    )

                    Spacer(Modifier.height(20.dp))

                    DefaultButton(
                        text = stringResource(R.string.continue_with_google),
                        isOutlined = true,
                        leadingIcon = {
                            Icon(
                                painterResource(R.drawable.ic_google),
                                null,
                                tint= Color.Unspecified,
                                modifier = Modifier.size(18.dp),

                                )
                        },
                        onClick = { /* ... */ }
                    )

                    Spacer(Modifier.height(20.dp))

                    DefaultButton(
                        text = stringResource(R.string.log_in_using_facebook),
                        isOutlined = true,
                        leadingIcon = {
                            Icon(
                                painterResource(R.drawable.ic_facebook),
                                null,
                                tint= Color.Unspecified,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        onClick = { /* ... */ }
                    )

                    Spacer(Modifier.height(24.dp))
                }
            }
        }

    }
}





