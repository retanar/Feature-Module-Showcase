package com.featuremodule.homeImpl.theming

import com.featuremodule.core.ui.UiEvent
import com.featuremodule.core.ui.UiState
import com.featuremodule.core.ui.theme.ColorsDark
import com.featuremodule.core.ui.theme.ColorsLight
import com.featuremodule.core.ui.theme.ThemeStyle

internal data class State(
    val isLoading: Boolean = true,
    val previewTheme: ThemeState = ThemeState(),
) : UiState

internal data class ThemeState(
    val colorsLight: ColorsLight = ColorsLight.Default,
    val colorsDark: ColorsDark = ColorsDark.Default,
    val themeStyle: ThemeStyle = ThemeStyle.System,
)

internal sealed interface Event : UiEvent {
    data class PreviewLightTheme(val colors: ColorsLight) : Event
    data class PreviewDarkTheme(val colors: ColorsDark) : Event
    data class SetThemeStyle(val themeStyle: ThemeStyle) : Event
    data class SaveTheme(val theme: ThemeState) : Event

    data object PopBackIfSaved : Event
    data object PopBack : Event
}
