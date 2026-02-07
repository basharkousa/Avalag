package com.bashar.avalag.src.features.main.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bashar.avalag.R


@Preview
@Composable
fun MainScreen(
    onNavigateToSettingScreen: () -> Unit = {},
//    viewModel: ViewModelTemplate = hiltViewModel()
) {

//    val state by viewModel.state

    val lastBackPressTime = remember { mutableStateOf(0L) }
    val context = LocalContext.current
//    val activity = (LocalContext.current as? Activity)

    Scaffold {
        Surface(
            modifier = Modifier.padding(it)

        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.clickable{
                    onNavigateToSettingScreen()
                }, contentAlignment = Alignment.Center) {
                    Text(
                        stringResource(R.string.main_screen),
                        
                    )
                }
            }
        }
    }

}