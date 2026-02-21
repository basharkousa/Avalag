package com.bashar.avalag.src.features.splash.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bashar.avalag.R
import com.bashar.avalag.src.core.ui.widgets.BackGroundThemeWidget
import com.bashar.avalag.src.core.utils.ScreenTemplate
import com.bashar.avalag.src.core.utils.UiText
import com.bashar.avalag.src.core.utils.asString
import com.bashar.avalag.src.features.splash.presentation.test.SplashTestTags


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(
    onNavigateToAuth: () -> Unit,
    onNavigateToMain: () -> Unit,
    onOpenUpdateLink: (String) -> Unit = {}, // keep it injectable from nav graph
    onExitApp: () -> Unit = {},              // keep it injectable from nav graph
    vm: SplashViewModel = hiltViewModel(),
) {
    val state by vm.state

    // Default implementations if caller didn't pass handlers
    val defaultOpenLink: (String) -> Unit = { link ->
        onOpenUpdateLink(link)
    }
    val defaultExit: () -> Unit = {
        onExitApp()
    }

    ScreenContent(
        state = state,
        onEvent = { event ->
            when (event) {
                SplashUiEvent.Retry -> vm.onEvent(SplashEvents.Retry)
                SplashUiEvent.ConsumeSnackbar -> vm.onEvent(SplashEvents.ConsumeSnackbar)

                SplashUiEvent.OpenUpdateLink -> {
                    val link = state.updateDialog?.link ?: return@ScreenContent
                    if (link.isBlank()) return@ScreenContent
                    if (onOpenUpdateLink === {}) defaultOpenLink(link) else onOpenUpdateLink(link)
                }

                SplashUiEvent.ExitApp -> {
                    if (onExitApp === {}) defaultExit() else onExitApp()
                }

                is SplashUiEvent.Navigate -> {
                    when (event.to) {
                        SplashDestination.AUTH -> onNavigateToAuth()
                        SplashDestination.MAIN -> onNavigateToMain()
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
internal fun ScreenContent(
    state: SplashState = SplashState(isLoading = true),
    onEvent: (SplashUiEvent) -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // navigation driven by state (route handles actual nav)
    LaunchedEffect(state.navigateTo) {
        state.navigateTo?.let { onEvent(SplashUiEvent.Navigate(it)) }
    }

    // error -> snackbar with Retry (indefinite) -> emit events
    LaunchedEffect(state.snackbarMessage) {
        val uiText = state.snackbarMessage ?: return@LaunchedEffect
        val msg = uiText.asString(context)
        val retryLabel = UiText.StringResource(R.string.retry).asString(context)

        val result = snackbarHostState.showSnackbar(
            message = msg,
            actionLabel = retryLabel,
            duration = SnackbarDuration.Indefinite
        )
        onEvent(SplashUiEvent.ConsumeSnackbar)
        if (result == SnackbarResult.ActionPerformed) {
            onEvent(SplashUiEvent.Retry)
        }
    }

    ScreenTemplate(
        snackbarHost = {
            // Custom snackbar host so Retry is taggable for UI tests
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    action = {
                        val label = data.visuals.actionLabel
                        if (!label.isNullOrBlank()) {
                            TextButton(
                                modifier = Modifier.testTag(SplashTestTags.SNACKBAR_RETRY),
                                onClick = { data.performAction() },
                            ) { Text(label, color = MaterialTheme.colorScheme.onPrimary) }
                        }
                    }
                ) { Text(data.visuals.message) }
            }
        },
        background = { BackGroundThemeWidget(Modifier.fillMaxSize()) },
    ) { padding ->
        SplashBody(padding = padding, isLoading = state.isLoading)

        // Mandatory update dialog
        //todo Add ForceUpdateDialog
        state.updateDialog?.let { dialog ->
            AlertDialog(
                modifier = Modifier.testTag(SplashTestTags.UPDATE_DIALOG),
                onDismissRequest = { /* blocked */ },
                title = { Text(stringResource(R.string.update_required)) },
                text = { Text(stringResource(R.string.update_required_message)) },
                confirmButton = {
                    TextButton(
                        modifier = Modifier.testTag(SplashTestTags.UPDATE_BTN),
                        enabled = !dialog.link.isNullOrBlank(),
                        onClick = { onEvent(SplashUiEvent.OpenUpdateLink) }
                    ) { Text(stringResource(R.string.update)) }
                },
                dismissButton = {
                    TextButton(
                        modifier = Modifier.testTag(SplashTestTags.EXIT_BTN),
                        onClick = { onEvent(SplashUiEvent.ExitApp) }
                    ) { Text(stringResource(R.string.exit)) }
                }
            )
        }
    }
}

@Composable
private fun SplashBody(
    padding: PaddingValues,
    isLoading: Boolean,
) {
    Box(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .width(96.33098.dp)
                .height(93.42924.dp)
        )

        Image(
            painter = painterResource(R.drawable.ic_avalag_logo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(41.51.dp)
                .width(197.00002.dp)
                .height(58.48578.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 24.dp)
                    .testTag(SplashTestTags.LOADING)
            )
        }
    }
}
