package com.bashar.avalag.src.features.main.presentation


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Person
import com.bashar.avalag.R

sealed class MainDestination(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
) {
    data object Home :
        MainDestination("home", R.string.home, R.drawable.ic_home_flat, R.drawable.ic_home_filled)

    data object Cart :
        MainDestination("cart", R.string.cart, R.drawable.ic_bag_flat, R.drawable.ic_bag_filled)

    data object Orders :
        MainDestination("orders", R.string.orders, R.drawable.ic_buy_flat, R.drawable.ic_buy_filled)

    data object Profile : MainDestination(
        "profile",
        R.string.profile,
        R.drawable.ic_profile_flat,
        R.drawable.ic_profile_filled
    )
}

val bottomDestinations = listOf(
    MainDestination.Home,
    MainDestination.Cart,
    MainDestination.Orders,
    MainDestination.Profile
)
