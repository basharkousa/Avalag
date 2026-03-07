package com.bashar.avalag.src.core.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bashar.avalag.src.features.auth.presentation.screens.forgotreset.ForgotScreen
import com.bashar.avalag.src.features.auth.presentation.screens.forgotreset.ResetScreen
import com.bashar.avalag.src.features.auth.presentation.screens.login.LoginScreen
import com.bashar.avalag.src.features.auth.presentation.screens.otp.OtpScreen
import com.bashar.avalag.src.features.auth.presentation.screens.signup.SignUpScreen
import com.bashar.avalag.src.features.main.presentation.MainScreen
import com.bashar.avalag.src.features.main.presentation.TestScreen
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
    startDestination: String = Screen.SplashRoute.route,
) {


    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = startDestination) {

            composable(Screen.SplashRoute.route) {
                SplashScreen(
                  onNavigateToAuth = {
                      navController.navigate(Screen.LoginScreenRoute.route) {
                          popUpTo(Screen.SplashRoute.route) { inclusive = true }
                      }
                  },
                  onNavigateToMain = {
                      navController.navigate(Screen.MainScreenRoute.route) {
                          popUpTo(Screen.SplashRoute.route) { inclusive = true }
                      }
                  },
                  onOpenUpdateLink = { link ->
                     navController.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                  },
                  onExitApp = {
                      ((navController.context) as? Activity)?.finish()
                  }
                )
            }
            composable(Screen.LoginScreenRoute.route) {
                LoginScreen(
                    onBack = {
                        navController.navigateUp()
                    },
//                    onNext = {
//                        navController.navigate(Screen.OtpRouteRoute.route)
//                    },
                    onSkip = {
                        navController.navigate(Screen.MainScreenRoute.route){
//                            popUpTo(Screen.LoginScreenRoute.route) { inclusive = true }
                        }
                    },
                    onNavigateToResetPassword = {
                        navController.navigate(Screen.ForgotRoute.route)
                    },
                    onNavigateToMain = {
                        navController.navigate(Screen.MainScreenRoute.route) {
                            popUpTo(Screen.SplashRoute.route) { inclusive = true }
                        }
                    },

                    onNavigateToSignup = {
                        navController.navigate(Screen.SignUpRouteRoute.route)
                    }

                )
            }
            composable(Screen.OtpRouteRoute.route) {
                OtpScreen(
                    onBack = {
                        navController.navigateUp()
                    },
                    onNext = {
                        navController.navigate(Screen.SettingScreenRoute.route)
                    }
                )
            }

            composable(Screen.SignUpRouteRoute.route) {
                SignUpScreen(
                    onBack = {
                        navController.navigateUp()
                    },
                    onNext = {
                        navController.navigate(Screen.OtpRouteRoute.route)
                    }
                )
            }

            //todo nested graph
            composable(Screen.ForgotRoute.route) {
                ForgotScreen(
                    onBack = {
                        navController.navigateUp()
                    },
                    onNext = {
                        navController.navigate(Screen.OtpRouteRoute.route)
                    }
                )
            }
            composable(Screen.ResetRoute.route) {
                ResetScreen(
                    onBack = {
                        navController.navigateUp()
                    },
                    onNext = {
                        navController.navigate(Screen.LoginScreenRoute.route){
                            popUpTo(Screen.ResetRoute.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.OnBoardingRoute.route) {
                OnBoardingScreen(
                    onNavigateToMain = {
                        navController.navigate(Screen.LoginScreenRoute.route) {
                            popUpTo(Screen.OnBoardingRoute.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.SettingScreenRoute.route) {
                SettingScreen(
                    onBack = {
                        navController.navigateUp()
                    }
                )
            }

            composable(Screen.MainScreenRoute.route) {

                val rootNavController = LocalNavController.current // this is your app-wide controller
                val tabNavController = rememberNavController()     // this is nested controller for tabs

                MainScreen(
                    tabNavController = tabNavController,
                    homeContent = {
                        TestScreen(title = "Home", onClick = {
                            // Navigate with ROOT controller to a global screen:
                            rootNavController.navigate(Screen.SettingScreenRoute.route)
                        })
                    },
                    cartContent = { TestScreen("Cart") },
                    ordersContent = { TestScreen("Orders") },
                    profileContent = { TestScreen("Profile") },
                )
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
