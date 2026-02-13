package com.bashar.avalag.src.core.ui.widgets.sections

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.bashar.avalag.R
import com.bashar.avalag.src.core.ui.widgets.Refreshable
import com.bashar.avalag.src.core.utils.ApiErrorParser
import com.bashar.avalag.src.core.utils.NetworkMonitor

@Composable
fun <T : Any> PaginatedSection(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<T>,
    onRefresh: () -> Unit = {},
    onRetry: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(12.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(12.dp),
    itemContent: @Composable (T) -> Unit
) {
    val context = LocalContext.current

    // Auto retry when network returns
    val isConnected by NetworkMonitor.observe(context)
        .collectAsState(initial = NetworkMonitor.isConnected(context))
    var wasOffline by remember { mutableStateOf(false) }

    LaunchedEffect(isConnected) {
        if (isConnected && wasOffline) {
            pagingItems.retry()
            wasOffline = false
        } else if (!isConnected) {
            wasOffline = true
        }
    }

    // Toast on refresh error
    val refreshState = pagingItems.loadState.refresh
    LaunchedEffect(refreshState) {
        if (refreshState is LoadState.Error) {
            val message = ApiErrorParser.getMessage(refreshState.error)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    val refreshing = refreshState is LoadState.Loading

    Refreshable(
        refreshing = refreshing,
        onRefresh = {
            onRefresh()
            pagingItems.refresh()
        }
    ) {
        Box(modifier = modifier.fillMaxSize()) {

            // Fullscreen loading (first load)
            if (refreshState is LoadState.Loading && pagingItems.itemCount == 0) {
                LoadingStateWidget()
                return@Box
            }

            // Fullscreen error (only when list is empty)
            if (refreshState is LoadState.Error && pagingItems.itemCount == 0) {
                ErrorStateWidget(
                    message = ApiErrorParser.getMessage(refreshState.error),
                    onRetry = {
                        pagingItems.retry()
                        onRetry()
                    }
                )
                return@Box
            }

            // List
            if (pagingItems.itemCount > 0) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = contentPadding,
                    verticalArrangement = verticalArrangement
                ) {
                    items(
                        count = pagingItems.itemCount,
                        key = { index -> index }
                    ) { index ->
                        val item = pagingItems[index]
                        if (item != null) itemContent(item)
                    }

                    when (val appendState = pagingItems.loadState.append) {
                        is LoadState.Loading -> item { ListFooterLoadingWidget() }
                        is LoadState.Error -> item {
                            ListFooterErrorWidget {
                                pagingItems.retry()
                                onRetry()
                            }
                        }
                        else -> Unit
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.no_items),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun ListFooterLoadingWidget() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }
}

@Composable
private fun LoadingStateWidget() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorStateWidget(message: String, onRetry: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(message)
            Spacer(Modifier.height(10.dp))
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}

@Composable
private fun ListFooterErrorWidget(onRetry: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        TextButton(onClick = onRetry) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(Modifier.padding(4.dp))
            Text(stringResource(R.string.retry))
        }
    }
}
