package com.bashar.avalag.src.core.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bashar.avalag.src.core.ui.theme.Primary100
import com.bashar.avalag.src.core.utils.AvalagMultiPreview
import com.bashar.avalag.src.features.main.presentation.MainDestination
import com.bashar.avalag.src.features.main.presentation.bottomDestinations

@AvalagMultiPreview
@Composable
fun FloatingBottomBar(
    currentDestinationRoute: String? = "cart",
    cartBadge: Int = 2,
    onTabClick: (MainDestination) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val items = bottomDestinations

    // Floating container (rounded + shadow + translucent)

    Surface(
        modifier = modifier
            .padding(horizontal = 0.dp, vertical = 0.dp),
        shape = RoundedCornerShape(0.dp).copy(
            topStart = CornerSize(22.dp),
            topEnd = CornerSize(22.dp)
        ),
//            tonalElevation = 0.dp,
        shadowElevation = 10.dp,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f) // translucency
    ) {
        NavigationBar(
            modifier = Modifier,
            containerColor = MaterialTheme.colorScheme.surface, // let Surface color show
            tonalElevation = 0.dp,
        ) {
            items.forEach { dest ->
                val selected = currentDestinationRoute == dest.route

                NavigationBarItem(
                    selected = selected,
                    onClick = { onTabClick(dest) },
                    icon = {
                        if (dest == MainDestination.Cart && cartBadge > 0) {
                            BadgedBox(badge = {
                                Badge {
                                    Text(
                                        cartBadge.coerceAtMost(99).toString()
                                    )
                                }
                            }) {
                                Icon(
                                    painter = painterResource(
                                        if (!selected) {
                                            dest.icon
                                        } else {
                                            dest.selectedIcon
                                        }
                                    ), contentDescription = null
                                )
                            }
                        } else {
                            Icon(
                                painterResource(
                                    if (!selected) {
                                        dest.icon
                                    } else {
                                        dest.selectedIcon
                                    }
                                ), contentDescription = null
                            )
                        }
                    },
                    label = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(id = dest.title),
                                fontWeight = if (selected) MaterialTheme.typography.titleMedium.fontWeight else MaterialTheme.typography.titleSmall.fontWeight
                            )
                        }
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f), // remove pill indicator
                        selectedIconColor = MaterialTheme.colorScheme.onSurface,
                        selectedTextColor = MaterialTheme.colorScheme.onSurface,
                        unselectedIconColor = Primary100,
                        unselectedTextColor = Primary100
                    )
                )
            }
        }
    }


}
