package com.bashar.avalag.src.core.utils
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bashar.avalag.R
import com.bashar.avalag.src.core.ui.widgets.BackGroundThemeWidget
import com.bashar.avalag.src.core.ui.widgets.Greeting
import com.bashar.avalag.src.core.utils.TemplateEvents
import com.bashar.avalag.src.core.utils.TemplateState
import com.bashar.avalag.src.core.utils.ViewModelTemplate


@Composable
fun ScreenTemplate(
//    onNavigateToScreen: (Int) -> Unit,
    viewModel: ViewModelTemplate = hiltViewModel()
) {
    val state by viewModel.state

    ScreenContent(
        state = state,
        onEvent = { event ->
            when(event) {
//                is TemplateEvent.OnNavigateToScreen -> onNavigateToScreen()
//                else -> viewModel.onEvent(event)
                else -> {}
            }
        }
    )
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ScreenContent(
    state: TemplateState = TemplateState(),
    onEvent: (TemplateEvents) -> Unit = {}
) {

    LaunchedEffect(Unit) {
//        if(!state.isLoading) {
//            onEvent(ScreenEvents.NavigateToScreen)
//        }
    }
    Scaffold() {
        BackGroundThemeWidget(Modifier.fillMaxSize())
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 34.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(weight = 1f))
                Image(
                    painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(107.dp)
                        .width(156.dp)
                )
                Spacer(modifier = Modifier.weight(weight = 0.8f))
                Greeting(
//                    name = stringResource(id = R.string.template_screen),
                    name = "Welcome to ${state.name}",
                    modifier = Modifier.padding()
                )
            }
        }
    }

}

