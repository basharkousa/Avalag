package com.bashar.avalag.src.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.bashar.avalag.R

// ===== Fonts =====
val WestMoscow = FontFamily(
    Font(R.font.west_moscow, weight = FontWeight.W400, style = FontStyle.Normal)
)

val Alexandria = FontFamily( // use this for Arabic
    Font(R.font.alexandria_regular, weight = FontWeight.W400, style = FontStyle.Normal)
)

val Avenir = FontFamily( // keep for Latin if needed elsewhere
    Font(R.font.avenir_regular, weight = FontWeight.W400, style = FontStyle.Normal),
    Font(R.font.avenir_medium,  weight = FontWeight.W500, style = FontStyle.Normal),
    Font(R.font.avenir_heavy,   weight = FontWeight.W600, style = FontStyle.Normal),
)

val Montserrat = FontFamily( // Latin body & labels
    Font(R.font.montserrat_regular,   weight = FontWeight.W400),
    Font(R.font.montserrat_medium,    weight = FontWeight.W500),
    Font(R.font.montserrat_semi_bold, weight = FontWeight.W600),
)

// ===== Helpers =====
/** Figma letter spacing in percent, e.g. 2f = 2% → 0.02em */
fun lsPercent(pct: Float): TextUnit = (pct / 100f).em
/** Figma line height in percent, e.g. 140f = 140% */
fun lhPercent(fontSizeSp: Int, pct: Float): TextUnit = (fontSizeSp * pct / 100f).sp

// ===== Latin (EN) Typography =====
// Headings (WestMoscow), Body/Labels (Montserrat)
val TypographyEn = Typography(
    // H1 48 / 120% / 0%
    displayLarge = TextStyle(
        fontFamily = WestMoscow, fontWeight = FontWeight.W400,
        fontSize = 48.sp, lineHeight = lhPercent(48, 120f), letterSpacing = lsPercent(0f)
    ),
    // H2 40 / 120%
    displayMedium = TextStyle(
        fontFamily = WestMoscow, fontWeight = FontWeight.W400,
        fontSize = 40.sp, lineHeight = lhPercent(40, 120f), letterSpacing = lsPercent(0f)
    ),
    // H3 32 / 120%
    displaySmall = TextStyle(
        fontFamily = WestMoscow, fontWeight = FontWeight.W400,
        fontSize = 32.sp, lineHeight = lhPercent(32, 120f), letterSpacing = lsPercent(0f)
    ),
    // H4 24 / 140%
    headlineLarge = TextStyle(
        fontFamily = WestMoscow, fontWeight = FontWeight.W400,
        fontSize = 24.sp, lineHeight = lhPercent(24, 140f), letterSpacing = lsPercent(0f)
    ),
    // H5 20 / 120%
    headlineMedium = TextStyle(
        fontFamily = WestMoscow, fontWeight = FontWeight.W400,
        fontSize = 20.sp, lineHeight = lhPercent(20, 120f), letterSpacing = lsPercent(0f)
    ),
    // H6 18 / 140%
    headlineSmall = TextStyle(
        fontFamily = WestMoscow, fontWeight = FontWeight.W400,
        fontSize = 18.sp, lineHeight = lhPercent(18, 140f), letterSpacing = lsPercent(0f)
    ),

    // Titles (Montserrat)
    titleMedium = TextStyle( // 20 / SemiBold
        fontFamily = Montserrat, fontWeight = FontWeight.W600,
        fontSize = 20.sp, lineHeight = lhPercent(20, 120f), letterSpacing = lsPercent(0f)
    ),
    titleSmall = TextStyle( // 18 / Medium or SemiBold per Figma; picked Medium by default
        fontFamily = Montserrat, fontWeight = FontWeight.W500,
        fontSize = 18.sp, lineHeight = lhPercent(18, 120f), letterSpacing = lsPercent(0f)
    ),

    // Body
    bodyLarge = TextStyle( // 18 Regular / 140% / 0%
        fontFamily = Montserrat, fontWeight = FontWeight.W400,
        fontSize = 18.sp, lineHeight = lhPercent(18, 140f), letterSpacing = lsPercent(0f)
    ),
    bodyMedium = TextStyle( // 16 Regular / 140% / 0%
        fontFamily = Montserrat, fontWeight = FontWeight.W400,
        fontSize = 16.sp, lineHeight = lhPercent(16, 140f), letterSpacing = lsPercent(0f)
    ),
    bodySmall = TextStyle( // 14 Regular / 155% / 0% (often ~22sp line height)
        fontFamily = Montserrat, fontWeight = FontWeight.W400,
        fontSize = 14.sp, lineHeight = lhPercent(14, 155f), letterSpacing = lsPercent(0f)
    ),

    // Labels (buttons, chips)
    labelLarge = TextStyle( // 14 Medium / ~140%
        fontFamily = Montserrat, fontWeight = FontWeight.W500,
        fontSize = 14.sp, lineHeight = lhPercent(14, 140f), letterSpacing = lsPercent(0f)
    ),
    labelMedium = TextStyle( // 12 SemiBold (if needed for small buttons)
        fontFamily = Montserrat, fontWeight = FontWeight.W600,
        fontSize = 12.sp, lineHeight = lhPercent(12, 140f), letterSpacing = lsPercent(0f)
    ),
    labelSmall = TextStyle( // 11–12 Regular (captions)
        fontFamily = Montserrat, fontWeight = FontWeight.W400,
        fontSize = 12.sp, lineHeight = lhPercent(12, 140f), letterSpacing = lsPercent(0f)
    ),
)

// ===== Arabic Typography =====
// Use Alexandria consistently; Arabic generally avoids negative tracking.
val TypographyAr = Typography(
    displayLarge = TypographyEn.displayLarge.copy(fontFamily = Alexandria),
    displayMedium = TypographyEn.displayMedium.copy(fontFamily = Alexandria),
    displaySmall = TypographyEn.displaySmall.copy(fontFamily = Alexandria),
    headlineLarge = TypographyEn.headlineLarge.copy(fontFamily = Alexandria),
    headlineMedium = TypographyEn.headlineMedium.copy(fontFamily = Alexandria),
    headlineSmall = TypographyEn.headlineSmall.copy(fontFamily = Alexandria),

    titleMedium = TypographyEn.titleMedium.copy(fontFamily = Alexandria),
    titleSmall  = TypographyEn.titleSmall.copy(fontFamily = Alexandria),

    bodyLarge   = TypographyEn.bodyLarge.copy(fontFamily = Alexandria, letterSpacing = 0.em),
    bodyMedium  = TypographyEn.bodyMedium.copy(fontFamily = Alexandria, letterSpacing = 0.em),
    bodySmall   = TypographyEn.bodySmall.copy(fontFamily = Alexandria, letterSpacing = 0.em),

    labelLarge  = TypographyEn.labelLarge.copy(fontFamily = Alexandria, letterSpacing = 0.em),
    labelMedium = TypographyEn.labelMedium.copy(fontFamily = Alexandria, letterSpacing = 0.em),
    labelSmall  = TypographyEn.labelSmall.copy(fontFamily = Alexandria, letterSpacing = 0.em),
)
