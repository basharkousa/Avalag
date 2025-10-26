package com.bashar.avalag.src.core.navigation


//object NavigationScreen {
//
//    const val FIRST = "first"
//    const val SECOND = "second"
//    const val THIRD = "third"
//
//}

sealed class Screen(val route: String) {

    data object SplashRoute : Screen("splashScreen")
    data object OnBoardingRoute : Screen("onBoardingScreen")
    data object MainScreenRoute : Screen("mainScreen")
    data object SettingScreenRoute : Screen("settingScreen")
    data object LoginScreenRoute : Screen("loginScreen")
    data object SignUpRouteRoute : Screen("signupScreen")
    data object OtpRouteRoute : Screen("otpRouteRoute")
    data object HomeScreenRoute : Screen("homeScreen")


/*    data object AlbumsScreen : Screen("albumsScreen/{artist}"){
        fun sendArtistName(name: String) = "albumsScreen/$name"
    }*/
    data object AlbumsDetailsScreen : Screen("albumsDetailsScreen/{album}"){
//        fun sendAlbum(album: Album) = "albumsDetailsScreen/${album}"
//        fun sendAlbum(album: String) = "albumsDetailsScreen/${album}"
//        fun sendTestModel(album: TestModel) = "albumsDetailsScreen/${album}"
    }
}