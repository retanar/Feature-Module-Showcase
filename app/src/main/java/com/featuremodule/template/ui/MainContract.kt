package com.featuremodule.template.ui

import androidx.compose.material3.ColorScheme
import com.featuremodule.core.navigation.NavCommand
import com.featuremodule.core.ui.UiEvent
import com.featuremodule.core.ui.UiState
import com.featuremodule.core.ui.theme.ColorsDark
import com.featuremodule.core.ui.theme.ColorsLight
import kotlinx.coroutines.flow.SharedFlow

internal data class State(
    val commands: SharedFlow<NavCommand>,
    val isLoaded: Boolean = false,
    val theme: ThemeState = ThemeState(),
) : UiState

internal data class ThemeState(
    val lightColorScheme: ColorScheme = ColorsLight.Default.scheme,
    val darkColorScheme: ColorScheme = ColorsDark.Default.scheme,
    val useSystemDarkTheme: Boolean = true,
)

internal sealed interface Event : UiEvent {
    data class OpenNavBarRoute(val route: String, val isSelected: Boolean) : Event
}
