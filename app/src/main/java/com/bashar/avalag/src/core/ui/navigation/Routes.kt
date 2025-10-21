package com.bashar.avalag.src.core.ui.navigation


//object NavigationScreen {
//
//    const val FIRST = "first"
//    const val SECOND = "second"
//    const val THIRD = "third"
//
//}

sealed class Screen(val route: String) {

    data object SplashScreen : Screen("splashScreen")
    data object OnBoardingScreen : Screen("onBoardingScreen")
    data object MainScreen : Screen("mainScreen")
    data object SettingScreen : Screen("settingScreen")
    data object HomeScreen : Screen("homeScreen")


/*    data object AlbumsScreen : Screen("albumsScreen/{artist}"){
        fun sendArtistName(name: String) = "albumsScreen/$name"
    }*/
    data object AlbumsDetailsScreen : Screen("albumsDetailsScreen/{album}"){
//        fun sendAlbum(album: Album) = "albumsDetailsScreen/${album}"
//        fun sendAlbum(album: String) = "albumsDetailsScreen/${album}"
//        fun sendTestModel(album: TestModel) = "albumsDetailsScreen/${album}"
    }
}