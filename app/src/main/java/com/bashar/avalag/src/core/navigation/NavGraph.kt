package com.bashar.avalag.src.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bashar.avalag.src.features.auth.presentation.screens.login.LoginScreen
import com.bashar.avalag.src.features.main.presentation.MainScreen
import com.bashar.avalag.src.features.onboarding.presentation.OnBoardingScreen
import com.bashar.avalag.src.features.setting.presentation.screens.SettingScreen
import com.bashar.avalag.src.features.splash.presentation.SplashScreen

val LocalNavController = compositionLocalOf<NavHostController>() {
//    navController
    throw IllegalStateException("NavController not provided")
}

@Composable
fun MyAppNavigator(
    navController: NavHostController = rememberNavController(),
    navigatorBottomNavigation: NavHostController,
    startDestination: String = Screen.SplashScreen.route,
    modifier: Modifier = Modifier,
) {


    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = startDestination) {

            composable(Screen.SplashScreen.route) {
                SplashScreen(
                    onNavigateToScreen = {
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.SplashScreen.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.LoginScreen.route) {
                LoginScreen(
                    onBack = {
                        navController.navigateUp()
                    },
                    onNavigateToScreen = {
                        navController.navigate(Screen.SettingScreen.route)
                    }
                )
            }

            composable(Screen.OnBoardingScreen.route) {
                OnBoardingScreen(
                    onNavigateToMain = {
                        navController.navigate(Screen.MainScreen.route) {
                            popUpTo(Screen.OnBoardingScreen.route) { inclusive = true }
                        }
                    }
                )
            }



            composable(Screen.SettingScreen.route) {
                SettingScreen(
                    onBack = {
                        navController.navigateUp()
                    }
                )
            }

            composable(Screen.MainScreen.route) {
                MainScreen(onNavigateToSettingScreen = {
                    navController.navigate(Screen.SettingScreen.route)
                })
            }


            /*            composable(
                            Screen.AlbumsDetailsScreen.route, arguments = listOf(
                                navArgument("album") {
            //                    type = NavType.SerializableType<Album>(Album::class.java)
            //                    type = AssetParamType()
            //                    type = NavType.StringType
                                    type = AlbumNavType()
            //                    type = NavType.SerializableType(TestModel::class.java)
                                },
                            )
                        ) { navBackStackEntry ->
                            AlbumDetailsScreen(navController)
                        }*/

        }
    }
    // Add a listener to detect route not found
    // Add a custom navigation event listener
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?.substringBeforeLast("/")
}
