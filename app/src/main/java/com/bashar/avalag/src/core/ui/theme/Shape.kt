package com.bashar.avalag.src.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Shapes.kt
 * Defines corner radii (rounded corners) used across components.
 * Based on Figma design: soft rounded corners for cards, buttons, dialogs, etc.
 */
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),   // e.g., small chips, icons
    small = RoundedCornerShape(8.dp),        // text fields, small buttons
    medium = RoundedCornerShape(12.dp),      // cards, containers
    large = RoundedCornerShape(16.dp,),       // dialogs, sheets
    extraLarge = RoundedCornerShape(24.dp),
    // modals, bottom sheets
)
