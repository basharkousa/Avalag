package com.bashar.avalag.src.features.auth.presentation.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bashar.avalag.src.core.ui.theme.Primary100

@Composable
fun OrDivider(label: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(Modifier.weight(1f), DividerDefaults.Thickness, DividerDefaults.color)
        Text(
            text = "  $label  ",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W600,
                fontSize = 17.sp
            ),
            color = Primary100
        )
        HorizontalDivider(Modifier.weight(1f))
    }
}
