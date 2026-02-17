package com.bashar.avalag.src.core.ui.widgets.dialogs
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ForceUpdateDialog(
    visible: Boolean,
    title: String,
    message: String,
    updateText: String,
    exitText: String,
    updateEnabled: Boolean,
    onUpdate: () -> Unit,
    onExit: () -> Unit,
) {
    if (!visible) return

    AlertDialog(
        onDismissRequest = { /* blocked */ },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                enabled = updateEnabled,
                onClick = onUpdate
            ) {
                Text(updateText)
            }
        },
        dismissButton = {
            TextButton(onClick = onExit) {
                Text(exitText)
            }
        }
    )
}
