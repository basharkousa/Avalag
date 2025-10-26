package com.bashar.avalag.src.core.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.bashar.avalag.R
import com.bashar.avalag.src.core.ui.theme.Primary100



// --- Simple model for the dropdown ---
data class CountryUi(
    val code: String,                 // e.g. "+974"
    val name: String,                 // e.g. "Qatar"
    @DrawableRes val flagRes: Int     // e.g. R.drawable.flag_qatar
)

// Optional default list (replace with your real assets/list)
fun defaultCountries() = listOf(
    CountryUi("+974", "Qatar", R.drawable.ic_ring),
    CountryUi("+971", "United Arab Emirates", R.drawable.ic_ring),
    CountryUi("+966", "Saudi Arabia", R.drawable.ic_ring)
)

/**
 * When isPhone = true -> renders Row(country dropdown + number text field)
 * Otherwise -> behaves exactly like your original DefaultTextField.
 */
@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    placeholder: String = "Password",
    label: String? = null,
    isPassword: Boolean = false,
    // NEW: phone mode controls
    isPhone: Boolean = false,
    country: CountryUi = defaultCountries().first(),
    onCountryChange: (CountryUi) -> Unit = {},
    countries: List<CountryUi> = defaultCountries(),

    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    shape: Shape = MaterialTheme.shapes.large,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurface
    ),
    onFocusChange: ((Boolean) -> Unit)? = null,
) {
    if (isPhone) {
        PhoneField(
            modifier = modifier,
            number = value,
            onNumberChange = onValueChange,
            country = country,
            onCountryChange = onCountryChange,
            countries = countries,
            placeholder = placeholder,
            enabled = enabled,
            shape = shape,
            textStyle = textStyle,
            onFocusChange = onFocusChange
        )
    } else {
        // --- your original text field (unchanged) ---
        var isPasswordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = singleLine,
            maxLines = maxLines,
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged { onFocusChange?.invoke(it.isFocused) },
            textStyle = textStyle,
            shape = shape,
            placeholder = {
                Text(
                    placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Primary100
                )
            },
            label = label?.let { { Text(it) } },
            visualTransformation = if (isPassword && !isPasswordVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType
            ),
            leadingIcon = leadingIcon,
            trailingIcon = {
                when {
                    isPassword -> {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                painterResource(
                                    if (isPasswordVisible) R.drawable.ic_visibility_off
                                    else R.drawable.ic_visibility
                                ),
                                contentDescription = null,
                                tint = Primary100,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    trailingIcon != null -> trailingIcon()
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                cursorColor = MaterialTheme.colorScheme.primary,
            )
        )
    }
}

/* -------------------- Phone field internals -------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhoneField(
    modifier: Modifier,
    number: String,
    onNumberChange: (String) -> Unit,
    country: CountryUi,
    onCountryChange: (CountryUi) -> Unit,
    countries: List<CountryUi>,
    placeholder: String,
    enabled: Boolean,
    shape: Shape,
    textStyle: TextStyle,
    onFocusChange: ((Boolean) -> Unit)?
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Country dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.weight(0.42f)
        ) {
            OutlinedTextField(
                value = country.code,
                onValueChange = {},
                readOnly = true,
                enabled = enabled,
                singleLine = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                textStyle = textStyle,
                shape = shape,
                leadingIcon = {
                    CountryFlag(flagRes = country.flagRes)
                },
                trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null) },
                placeholder = { Text(stringResource(R.string.code), style = MaterialTheme.typography.bodyMedium, color = Primary100) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    cursorColor = MaterialTheme.colorScheme.primary,
                )
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                countries.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CountryFlag(flagRes = item.flagRes)
                                Spacer(Modifier.width(8.dp))
                                Text("${item.name}  ${item.code}")
                            }
                        },
                        onClick = {
                            onCountryChange(item)
                            expanded = false
                        }
                    )
                }
            }
        }

        // Phone number field
        OutlinedTextField(
            value = number,
            onValueChange = onNumberChange,
            enabled = enabled,
            singleLine = true,
            modifier = Modifier
                .weight(0.58f)
                .fillMaxWidth()
                .onFocusChanged { onFocusChange?.invoke(it.isFocused) },
            textStyle = textStyle,
            shape = shape,
            placeholder = {
                Text(placeholder.ifBlank { "phone number" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Primary100)
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                cursorColor = MaterialTheme.colorScheme.primary,
            )
        )
    }
}

@Composable
private fun CountryFlag(@DrawableRes flagRes: Int) {
    Box(
        Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(flagRes),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = Color.Unspecified // show real colors if your asset is colored
        )
    }
}
