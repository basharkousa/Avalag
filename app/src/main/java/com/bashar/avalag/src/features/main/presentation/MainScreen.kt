package com.bashar.avalag.src.features.main.presentation

// main/presentation/MainScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.bashar.avalag.src.core.ui.theme.AvalagTheme
import com.bashar.avalag.src.core.utils.AvalagMultiPreview
import kotlinx.coroutines.flow.StateFlow
import com.bashar.avalag.src.core.ui.widgets.FloatingBottomBar

@AvalagMultiPreview()
@Composable
fun MainScreen(
    cartBadgeCount: StateFlow<Int>? = null, // optional: pass from VM if you have it
    startDestination: String = MainDestination.Home.route,
    homeContent: @Composable () -> Unit = {},
    cartContent: @Composable () -> Unit = {},
    ordersContent: @Composable () -> Unit = {},
    profileContent: @Composable () -> Unit = {}
) {
    val navController = rememberNavController()
    val backstackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backstackEntry?.destination

    val badge by cartBadgeCount?.collectAsState(initial = 0) ?: remember { mutableStateOf(0) }


    Scaffold(
//        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
        bottomBar = {
            val currentRoute = currentDestination?.route
            FloatingBottomBar(
                currentDestinationRoute = currentRoute,
                cartBadge = badge,
                onTabClick = { dest ->
                    navController.navigate(dest.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainDestination.Home.route)   { homeContent() }
            composable(MainDestination.Cart.route)   { cartContent() }
            composable(MainDestination.Orders.route) { ordersContent() }
            composable(MainDestination.Profile.route){ profileContent() }
        }
    }

}

private fun NavDestination?.isOnDestination(route: String): Boolean {
    return this?.hierarchy?.any { it.route == route } == true
}
