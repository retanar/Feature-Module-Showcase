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
    switchToDarkWithSystem: Boolean,
    isSystemDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (switchToDarkWithSystem && isSystemDark) {
        colorsDark
    } else {
        colorsLight
    }

    // Ignores whether color scheme is dark
    ProvideAppColors(isSystemDark) {
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
