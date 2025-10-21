package com.bashar.avalag.src.core.ui.widgets

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun StatusBarVisible(visible: Boolean) {
    val activity = LocalActivity.current as Activity
    DisposableEffect(visible) {
        val window = activity.window
        val controller = WindowInsetsControllerCompat(window, window.decorView)

        // Recommended when you manage system bars yourself
        WindowCompat.setDecorFitsSystemWindows(window, /* decorFitsSystemWindows = */ !visible)

        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        if (visible) {
            controller.show(WindowInsetsCompat.Type.statusBars())
        } else {
            controller.hide(WindowInsetsCompat.Type.statusBars())
        }

        onDispose {
            // Ensure bars are restored if this Composable leaves
            controller.show(WindowInsetsCompat.Type.statusBars())
            WindowCompat.setDecorFitsSystemWindows(window, true)
        }
    }
}
