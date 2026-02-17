package com.bashar.avalag.src.features.auth.presentation.screens.otp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bashar.avalag.R
import com.bashar.avalag.src.core.ui.theme.Primary100
import com.bashar.avalag.src.core.ui.widgets.DefaultButton
import com.bashar.avalag.src.core.utils.DemoEvents
import com.bashar.avalag.src.core.utils.DemoState
import com.bashar.avalag.src.core.utils.DemoViewModel
import com.bashar.avalag.src.features.auth.presentation.widgets.AuthTppBar
import com.bashar.avalag.src.features.auth.presentation.widgets.OtpField


@Composable
fun OtpScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: DemoViewModel = hiltViewModel()
) {
    val state by viewModel.state

    ScreenContent(
        state = state,
        onEvent = { event ->
            when (event) {
                is DemoEvents.OnBackPress -> onBack()
                DemoEvents.OnNavigateToScreen -> onNext()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ScreenContent(
    state: DemoState = DemoState(),
    onEvent: (DemoEvents) -> Unit = {}
) {

    var otp by remember { mutableStateOf("") }
    val otpError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AuthTppBar(stringResource = R.string.activation_code, onBack = {
                onEvent(DemoEvents.OnBackPress)
            },)
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
                    text = stringResource(R.string.please_enter_the_code_we_sent_to_number),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Primary100,
                        fontWeight = FontWeight.W500
                    )
                )

                Spacer(Modifier.height(40.dp))

                // Phone row (country + phone)
                OtpField(
//                    value = state.otp,                           // add otp: String = "" to TemplateState
                    value = otp,                           // add otp: String = "" to TemplateState
                    onValueChange = { code ->
                        otp = code
//                        onEvent(TemplateEvents.OnOtpChanged(code)) // add this event in your template
                    },
                    length = 6,
//                    isError = state.otpError,                    // optional: add otpError: Boolean
                    isError = otpError,                    // optional: add otpError: Boolean
                    onFilled = { code ->
                        // Called automatically when all digits are entered or pasted
//                        onEvent(TemplateEvents.OnOtpCompleted(code))
                    }
                )

                Spacer(Modifier.height(30.dp))

                // Primary button
                DefaultButton(
                    text = stringResource(R.string.next),
                    onClick = { onEvent(DemoEvents.OnNavigateToScreen) }
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}




