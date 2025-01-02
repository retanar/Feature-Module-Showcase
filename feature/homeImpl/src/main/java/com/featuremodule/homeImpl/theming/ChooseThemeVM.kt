package com.featuremodule.homeImpl.theming

import com.featuremodule.core.navigation.NavCommand
import com.featuremodule.core.navigation.NavManager
import com.featuremodule.core.ui.BaseVM
import com.featuremodule.core.ui.theme.ColorsDark
import com.featuremodule.core.ui.theme.ColorsLight
import com.featuremodule.core.ui.theme.ThemeStyle
import com.featuremodule.data.prefs.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ChooseThemeVM @Inject constructor(
    private val themePreferences: ThemePreferences,
    private val navManager: NavManager,
) : BaseVM<State, Event>() {
    private lateinit var savedTheme: ThemeState

    init {
        launch {
            loadSavedTheme()
            setState { copy(previewTheme = savedTheme, isLoading = false) }
        }
    }

    private fun loadSavedTheme() {
        savedTheme = themePreferences.getCurrentPreferences().toThemeState()
    }

    // Light colors can be assigned dark colors if user sets dark theme with no switching
    private fun ThemePreferences.ThemeModel.toThemeState() = ThemeState(
        colorsLight = ColorsLight.entries.find { it.name == lightTheme }
            ?: ColorsLight.Default,
        colorsDark = ColorsDark.entries.find { it.name == darkTheme }
            ?: ColorsDark.Default,
        themeStyle = ThemeStyle.entries.find { it.name == themeStyle } ?: ThemeStyle.System,
    )

    override fun initialState() = State()

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.PreviewLightTheme -> setState {
                copy(previewTheme = previewTheme.copy(colorsLight = event.colors))
            }

            is Event.PreviewDarkTheme -> setState {
                copy(previewTheme = previewTheme.copy(colorsDark = event.colors))
            }

            is Event.SetThemeStyle -> setState {
                copy(previewTheme = previewTheme.copy(themeStyle = event.themeStyle))
            }

            is Event.SaveTheme -> saveTheme()

            Event.PopBackIfSaved -> {
                if (state.value.previewTheme == savedTheme) {
                    launch { navManager.navigate(NavCommand.PopBack) }
                    return
                }
            }

            Event.PopBack -> launch { navManager.navigate(NavCommand.PopBack) }
        }
    }

    private fun saveTheme() = with(state.value.previewTheme) {
        themePreferences.setLightTheme(colorsLight.name)
        themePreferences.setDarkTheme(colorsDark.name)
        themePreferences.setThemeStyle(themeStyle.name)
    }
}
