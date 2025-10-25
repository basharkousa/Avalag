package com.bashar.avalag.src.features.setting.presentation.widgets.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bashar.avalag.R
import com.bashar.avalag.src.features.setting.domain.models.ThemeMode

@Composable
fun ThemePickerDialog(
    selected: ThemeMode, onSelect: (ThemeMode) -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.theme_mode)) },
        text = {
            Column {
                ThemeMode.entries.forEach { mode ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(mode) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = mode == selected, onClick = { onSelect(mode) })
                        Spacer(Modifier.width(8.dp))
                        Text(
                            when (mode) {
                                ThemeMode.SYSTEM -> stringResource(R.string.system_default)
                                ThemeMode.LIGHT -> stringResource(R.string.light)
                                ThemeMode.DARK -> stringResource(R.string.dark)
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.close)) } }
    )
}