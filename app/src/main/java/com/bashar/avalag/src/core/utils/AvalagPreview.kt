@file:Suppress("unused")
package com.bashar.avalag.src.core.utils

import com.bashar.avalag.src.core.ui.theme.AvalagTheme
import android.content.res.Configuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

// 1) Flags you can expand later (dynamic colors, font scale, etc.)
data class AvalagPreviewFlags(
    val darkTheme: Boolean = false,
    val langTag: String = "en" // "ar" for Arabic
)

// 2) Provider generates combinations you want to preview
class AvalagPreviewFlagsProvider : PreviewParameterProvider<AvalagPreviewFlags> {
    override val values = sequenceOf(
        AvalagPreviewFlags(darkTheme = false, langTag = "en"),
        AvalagPreviewFlags(darkTheme = true,  langTag = "en"),
        AvalagPreviewFlags(darkTheme = false, langTag = "ar"),
        AvalagPreviewFlags(darkTheme = true,  langTag = "ar"),
    )
}

/**
 * 3) Your custom preview wrapper.
 *
 * Notes:
 * - Compose Preview doesn't truly switch app locale globally like runtime.
 * - If your strings come from stringResource(), it uses the Preview's locale.
 * - So: for language, we rely on @Preview(locale = "...") variants below.
 * - The flags still carry langTag so your UI can choose mock localized content if needed.
 */
@Composable
fun AvalagPreview(
    flags: AvalagPreviewFlags = AvalagPreviewFlags(),
    content: @Composable () -> Unit
) {
    AvalagTheme(
        darkTheme = flags.darkTheme,
        // add any other theme switches you have:
        // dynamicColor = flags.dynamicColor,
    ) {
        content()
    }
}

// 4) Convenience previews: Light/Dark + EN/AR
@Preview(
    name = "EN • Light",
    showBackground = true,
    locale = "en",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "EN • Dark",
    showBackground = true,
    locale = "en",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "AR • Light",
    showBackground = true,
    locale = "ar",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "AR • Dark",
    showBackground = true,
    locale = "ar",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class AvalagMultiPreview

// 5) Example usage
@AvalagMultiPreview
@Composable
private fun ExampleScreenPreview() {
    // For multi-preview annotations, you can just pick a default wrapper:
    AvalagTheme(darkTheme = false){
        Text("Hello")
    }
}

// 6) Or: PreviewParameter-based (useful if you want many permutations)
@Preview(name = "Parameterized", showBackground = true)
@Composable
private fun ExampleParameterizedPreview(
    @PreviewParameter(AvalagPreviewFlagsProvider::class) flags: AvalagPreviewFlags
) {
    AvalagPreview(flags){
        // ScreenContent()
    }
}
