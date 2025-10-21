package com.bashar.avalag.src.core.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.bashar.avalag.R

@Composable
fun BackGroundThemeWidget(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.iv_splash_black),
        contentScale = ContentScale.FillWidth,
        contentDescription = "")
}