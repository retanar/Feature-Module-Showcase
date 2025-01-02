package com.featuremodule.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun AppTheme(
    colorsLight: ColorScheme,
    colorsDark: ColorScheme,
    themeStyle: ThemeStyle,
    content: @Composable () -> Unit,
) {
    val isStyleDark = when (themeStyle) {
        ThemeStyle.Light -> false
        ThemeStyle.Dark -> true
        ThemeStyle.System -> isSystemInDarkTheme()
    }
    val colorScheme = if (isStyleDark) colorsDark else colorsLight

    ProvideAppColors(isStyleDark) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}

enum class ColorsLight(val scheme: ColorScheme) {
    Default(
        lightColorScheme(
            primary = Purple40,
            secondary = PurpleGrey40,
            tertiary = Pink40,
        ),
    )
}

enum class ColorsDark(val scheme: ColorScheme) {
    Default(
        darkColorScheme(
            primary = Purple80,
            secondary = PurpleGrey80,
            tertiary = Pink80,
        ),
    )
}

/** Sets light or dark theme as active, or switches it with system */
enum class ThemeStyle {
    Light,
    Dark,
    System
}

/**
 * A copy of [MaterialTheme] object with only custom colors. Can be responsible for all design,
 * if needed.
 */
object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current
}
