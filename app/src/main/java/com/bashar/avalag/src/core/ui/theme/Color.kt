package com.bashar.avalag.src.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val brandColor = Color(0xFF464936)

val primary500 = Color(0xFF000000)
val primary300 = Color(0xFF40424D)
val primary200 = Color(0xFF6E7180)
val Primary100 = Color(0xFF9DA2B3)
val primary50 = Color(0xFFBCBFCC)
val primary25 = Color(0xFFD3D6E0)
val primaryWhite = Color(0xFFFFFFFF)

val Primary0 = Color(0xFFEDEFF7) // light background
val Primary400 = Color(0xFF1E1E24) // dark background

val LightSecondary = Color(0xFFB9D7CA)
val Tertiary = Color(0xFF9CC0FA)

val LightSurface = Color(0xFFFFFFFF)
val DarkSurface = Color(0xFF26262C)

val OnLightBackground = Color(0xFF1C1B1F)
val OnLightSurface = Color(0xFF1C1B1F)

val OnDarkBackground = Color(0xFFE8E9EE)
val OnDarkSurface = Color(0xFFE8E9EE)

val OnLightPrimary = Color(0xFFFFFFFF)
val OnDarkPrimary = Color(0xFF000000) // if primary is light in dark

// Optional semantic sets
val alertSuccess300 = Color(0xFF22C55E)
val alertError300 = Color(0xFFDC2626)
val alertWarning300 = Color(0xFFCA8A04)

// ===== Material 3 Color Schemes =====
val LightColorScheme = lightColorScheme(
    primary = brandColor,         // main brand on light
    onPrimary = OnLightPrimary,
    secondary = LightSecondary,
    tertiary = Tertiary,
    background = Primary0,
    onBackground = OnLightBackground,
    surface = LightSurface,
    onSurface = OnLightSurface,
    // optional but good defaults
    error = alertError300,
    surfaceTint = Color.Unspecified

)

val DarkColorScheme = darkColorScheme(
    primary = Primary100,         // a lighter brand tone for dark
    onPrimary = OnDarkPrimary,
    primaryContainer = Primary100,
    onPrimaryContainer = OnDarkPrimary,
    secondary = LightSecondary,   // keep if it passes contrast
    tertiary = Tertiary,

    background = Primary400,
    onBackground = OnDarkBackground,
    surface = DarkSurface,
    onSurface = OnDarkSurface,

    error = alertError300,
    surfaceTint = Color.Unspecified
)
