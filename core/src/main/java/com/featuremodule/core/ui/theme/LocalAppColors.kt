package com.featuremodule.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// staticCompositionLocalOf works better for mostly unchanging colors
val LocalAppColors = staticCompositionLocalOf { AppColors() }

/**
 * Draft for providing own color hierarchy to be used in the same way as MaterialTheme.
 */
data class AppColors(
    val primary: Color = ColorsLight.Default.scheme.primary,
    val secondary: Color = ColorsLight.Default.scheme.secondary,
    val tertiary: Color = ColorsLight.Default.scheme.tertiary,
)

private val LightAppColors = AppColors()

private val DarkAppColors = AppColors(
    primary = ColorsDark.Default.scheme.primary,
    secondary = ColorsDark.Default.scheme.secondary,
    tertiary = ColorsDark.Default.scheme.tertiary,
)

@Composable
internal fun ProvideAppColors(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (isDark) DarkAppColors else LightAppColors

    CompositionLocalProvider(
        value = LocalAppColors provides colors,
        content = content,
    )
}
