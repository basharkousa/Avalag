package com.bashar.avalag.src

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.bashar.avalag.src.core.navigation.MyAppNavigator
import com.bashar.avalag.src.core.navigation.Screen
import com.bashar.avalag.src.core.navigation.currentRoute
import com.bashar.avalag.src.core.ui.theme.AvalagTheme
import com.bashar.avalag.src.features.setting.domain.models.ThemeMode
import com.bashar.avalag.src.features.setting.presentation.screens.SettingsViewModel
import com.bashar.avalag.R


@Composable
fun MyApp(vm: SettingsViewModel = hiltViewModel(), content: @Composable () -> Unit = {}) {

    val navController = rememberNavController()

    val textMessage = stringResource(R.string.press_back_again_to_exit)

    val lastBackPressTime = remember { mutableStateOf(0L) }
    val context = LocalContext.current
    val activity = (LocalActivity.current)

    val settingState by vm.state.collectAsState()
    val dark = when (settingState.theme) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val language = settingState.language


    AvalagTheme(
        darkTheme = dark,
        language  = language
    ) {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                MyAppNavigator(navController,)
                Surface {
//                    val systemUiController: SystemUiControll = rememberSystemUiController()
//                    systemUiController.isStatusBarVisible =
//                        currentRoute(navController = navController) !== Screen.SplashScreen.route

                    // Hide status bar on Splash only
//                    val isSplash = currentRoute(navController) == Screen.SplashScreen.route
//                    StatusBarVisible(visible = !isSplash)

                    BackHandler(enabled = (currentRoute(navController) === Screen.MainScreenRoute.route)) {

                        val currentTime = System.currentTimeMillis()
                        println("currentTime: $currentTime")
                        if (currentTime - lastBackPressTime.value < 2000) {
                            // Exit the app
                            activity?.finish()
                        } else {
                            // Update the time of the last back press and show a message to the user
                            lastBackPressTime.value = currentTime
                            Toast.makeText(context, textMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

    }
}
