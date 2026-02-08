package com.bashar.avalag.src.core.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DefaultButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isOutlined: Boolean = false,
    enabled: Boolean = true,
    loading: Boolean = false,
    shape: Shape = if (isOutlined) MaterialTheme.shapes.large else RoundedCornerShape(28.dp),
    height: Dp = 52.dp,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,

    // You can override colors if needed
    filledContainerColor: Color = MaterialTheme.colorScheme.onBackground,
    filledContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    outlinedContainerColor: Color = MaterialTheme.colorScheme.background,
    outlinedContentColor: Color = MaterialTheme.colorScheme.onSurface,
    outlinedBorderColor: Color = MaterialTheme.colorScheme.outlineVariant
) {
    val content: @Composable RowScope.() -> Unit = {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = if (isOutlined) outlinedContentColor else filledContentColor
            )
        } else {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(Modifier.width(8.dp))
            }
            Text(text)
            if (trailingIcon != null) {
                Spacer(Modifier.width(8.dp))
                trailingIcon()
            }
        }
    }

    val baseModifier = modifier
        .fillMaxWidth()
        .height(height)

    if (isOutlined) {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled && !loading,
            modifier = baseModifier,
            shape = shape,
            border = BorderStroke(1.dp, outlinedBorderColor),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = outlinedContainerColor,
                contentColor = outlinedContentColor,
                disabledContentColor = outlinedContentColor.copy(alpha = 0.38f)
            ),
            content = content
        )
    } else {
        Button(
            onClick = onClick,
            enabled = enabled && !loading,
            modifier = baseModifier,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = filledContainerColor,
                contentColor = filledContentColor,
                disabledContainerColor = filledContainerColor.copy(alpha = 0.38f),
                disabledContentColor = filledContentColor.copy(alpha = 0.38f)
            ),
            content = content
        )
    }
}
