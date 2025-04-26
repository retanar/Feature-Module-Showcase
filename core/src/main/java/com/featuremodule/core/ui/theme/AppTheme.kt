package com.featuremodule.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !isStyleDark
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightNavigationBars = !isStyleDark
        }
    }

    ProvideAppColors(isStyleDark) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
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
