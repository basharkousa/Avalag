package com.bashar.avalag.src.features.auth.presentation.screens.signup

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
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bashar.avalag.R
import com.bashar.avalag.src.core.ui.theme.Primary100
import com.bashar.avalag.src.core.ui.widgets.DefaultButton
import com.bashar.avalag.src.core.ui.widgets.DefaultTextField
import com.bashar.avalag.src.core.ui.widgets.defaultCountries
import com.bashar.avalag.src.core.utils.TemplateEvents
import com.bashar.avalag.src.core.utils.TemplateState
import com.bashar.avalag.src.core.utils.ViewModelTemplate
import com.bashar.avalag.src.features.auth.presentation.widgets.AuthTppBar


@Composable
fun SignUpScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: ViewModelTemplate = hiltViewModel()
) {
    val state by viewModel.state

    ScreenContent(
        state = state,
        onEvent = { event ->
            when (event) {
                is TemplateEvents.OnBackPress -> onBack()
//                else -> viewModel.onEvent(event)
                TemplateEvents.OnNavigateToScreen -> onNext()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ScreenContent(
    state: TemplateState = TemplateState(),
    onEvent: (TemplateEvents) -> Unit = {}
) {
    // Local preview state only (ignore VM)
    var countryExpanded by remember { mutableStateOf(false) }
    var selectedCode by remember { mutableStateOf("+974") }
    var phone by remember { mutableStateOf("") }
    var country by remember { mutableStateOf(defaultCountries().first()) }

    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AuthTppBar(stringResource = R.string.sign_up, onBack = {
                onEvent(TemplateEvents.OnBackPress)
            },)
//            LoginTopBar(
//                title = "Login",
//                onBack = { /* TODO hook up */ },
//                onSkip = { /* TODO hook up */ }
//            )
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
                    text = stringResource(R.string.please_enter_your_phone_number),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Primary100,
                        fontWeight = FontWeight.W500
                    )
                )

                Spacer(Modifier.height(40.dp))

                // Phone row (country + phone)
                DefaultTextField(
                    isPhone = true,
                    country = country,
                    onCountryChange = { country = it },
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = stringResource(R.string.phone_number)
                )

                Spacer(Modifier.height(12.dp))

               /* // Password
                DefaultTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = stringResource(R.string.password),
                    isPassword = true
                )*/

                Spacer(Modifier.height(51.dp))

                // Primary button
                DefaultButton(
                    text = stringResource(R.string.next),
                    onClick = { onEvent(TemplateEvents.OnNavigateToScreen) }
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}




