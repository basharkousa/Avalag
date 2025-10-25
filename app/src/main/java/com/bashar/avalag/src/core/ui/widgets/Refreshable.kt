package com.bashar.avalag.src.core.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.pulltorefresh.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Refreshable(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    val state = rememberPullToRefreshState()
    Box(Modifier.fillMaxSize().pullToRefresh(isRefreshing = refreshing, state = state, onRefresh = onRefresh)) {
        PullToRefreshBox(
            isRefreshing = refreshing,
            state = state,
            modifier = Modifier.align(Alignment.TopCenter),
            onRefresh = onRefresh,
        ){
            content()
        }
    }
}
