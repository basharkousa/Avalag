package com.bashar.avalag.src.core.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A lightweight reusable screen scaffold template:
 * - optional topBar
 * - optional snackbarHost
 * - optional background
 * - content gets PaddingValues
 *
 * This makes screens consistent and makes UI tests easier (pure ScreenContent).
 */
@Composable
fun ScreenTemplate(
    modifier: Modifier = Modifier,
    topBar: (@Composable () -> Unit)? = null,
    snackbarHost: (@Composable () -> Unit)? = null,
    background: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = { topBar?.invoke() },
        snackbarHost = { snackbarHost?.invoke() },
    ) { padding ->
        Box(Modifier.fillMaxSize()) {
            background?.invoke()
            content(padding)
        }
    }
}
