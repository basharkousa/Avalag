package com.bashar.avalag.src.features.auth.presentation.widgets

import android.R.attr.contentDescription
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun OtpField(
    value: String,
    onValueChange: (String) -> Unit,
    length: Int = 6,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    enabled: Boolean = true,
    onFilled: (String) -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val filtered = remember(value, length) {
        value.filter { it.isDigit() }.take(length)
    }

    // forward normalized value to VM
    LaunchedEffect(filtered) {
        if (filtered != value) onValueChange(filtered)
        if (filtered.length == length) onFilled(filtered)
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = filtered,
            onValueChange = { input ->
                // allow paste, keep only digits up to length
                val next = input.filter(Char::isDigit).take(length)
                onValueChange(next)
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .semantics {
// todo                   contentDescription = "Activation code input"
                },
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { if (filtered.length == length) focusManager.clearFocus() }
            ),
            singleLine = true,
            cursorBrush = SolidColor(Color.Unspecified), // hide caret
            decorationBox = { innerTextField ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(length) { index ->
                        val char = filtered.getOrNull(index)?.toString() ?: ""
                        val focused = filtered.length == index // simple focus heuristic

                        val borderColor = when {
                            isError -> MaterialTheme.colorScheme.error
                            focused -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.outline
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .border(
                                    width = 1.5.dp,
                                    color = borderColor,
                                    shape = MaterialTheme.shapes.large
                                )
                                .clip(MaterialTheme.shapes.large)
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable(enabled = enabled) { /* bring focus */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char.ifEmpty { "" },
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.W600
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                // Invisible field overlay to actually capture input
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(0.dp),
                    contentAlignment = Alignment.Center
                ) { innerTextField() }
            }
        )
    }

    LaunchedEffect(Unit) {
        // request focus when the field shows up
        focusRequester.requestFocus()
    }
}
