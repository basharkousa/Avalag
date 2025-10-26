package com.bashar.avalag.src.features.auth.presentation.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bashar.avalag.R
import com.bashar.avalag.src.core.ui.theme.WestMoscow


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthTppBar(
    modifier: Modifier = Modifier,
    @StringRes stringResource: Int? = null,
    title: String = "Title",
    canSkip: Boolean = false,
    onBack: () -> Unit = {},
    onSkip: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                if (stringResource != null) stringResource(stringResource) else title,
                style = MaterialTheme.typography.displaySmall
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "")
            }
        }, actions = {
            if(canSkip) Text(
                stringResource(R.string.skip),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W500),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(all = 8.dp)
                    .clickable {
                        onSkip()
                    })
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}