package com.bashar.avalag.src.features.main.presentation

// main/presentation/MainScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import kotlinx.coroutines.flow.StateFlow
import com.bashar.avalag.src.core.ui.widgets.FloatingBottomBar

@Composable
fun MainScreen(
    tabNavController: NavHostController,
    cartBadgeCount: StateFlow<Int>? = null,
    startDestination: String = MainDestination.Home.route,
    homeContent: @Composable () -> Unit = {},
    cartContent: @Composable () -> Unit = {},
    ordersContent: @Composable () -> Unit = {},
    profileContent: @Composable () -> Unit = {},
) {
    val backstackEntry by tabNavController.currentBackStackEntryAsState()
    val currentDestination = backstackEntry?.destination
    val badge by cartBadgeCount?.collectAsState(initial = 0) ?: remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            FloatingBottomBar(
                currentDestinationRoute = currentDestination?.route,
                cartBadge = badge,
                onTabClick = { dest ->
                    tabNavController.navigate(dest.route) {
                        popUpTo(tabNavController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainDestination.Home.route) { homeContent() }
            composable(MainDestination.Cart.route) { cartContent() }
            composable(MainDestination.Orders.route) { ordersContent() }
            composable(MainDestination.Profile.route) { profileContent() }
        }
    }
}


