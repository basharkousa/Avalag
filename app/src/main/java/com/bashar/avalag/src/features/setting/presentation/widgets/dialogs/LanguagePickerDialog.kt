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
import com.bashar.avalag.src.features.setting.domain.models.Language

@Composable
fun LanguagePickerDialog(
    selected: Language, onSelect: (Language) -> Unit, onDismiss: () -> Unit
) {
    val options = listOf(Language.System, Language.English, Language.Arabic)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change language") },
        text = {
            Column {
                options.forEach { lang ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(lang) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = lang == selected, onClick = { onSelect(lang) })
                        Spacer(Modifier.width(8.dp))
                        Text(
                            when (lang) {
                                Language.System -> stringResource(R.string.system_default)
                                Language.English -> "English"
//                                Language.French -> "Français"
                                Language.Arabic -> "العربية"
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.colse)) } }
    )
}
