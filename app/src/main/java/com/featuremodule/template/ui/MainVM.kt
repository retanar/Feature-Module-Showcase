package com.featuremodule.template.ui

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
internal class MainVM @Inject constructor(
    private val navManager: NavManager,
    private val themePreferences: ThemePreferences,
) : BaseVM<State, Event>() {
    init {
        launch {
            // The only loading for now is theme loading, so isLoaded is set together
            themePreferences.themeModelFlow.collect {
                setState { copy(theme = it.toThemeState(), isLoaded = true) }
            }
        }
    }

    private fun ThemePreferences.ThemeModel.toThemeState() = ThemeState(
        colorsLight = ColorsLight.entries.find { it.name == lightTheme }?.scheme
            ?: ColorsLight.Default.scheme,
        colorsDark = ColorsDark.entries.find { it.name == darkTheme }?.scheme
            ?: ColorsDark.Default.scheme,
        themeStyle = ThemeStyle.entries.find { it.name == themeStyle } ?: ThemeStyle.System,
    )

    override fun initialState() = State(navManager.commands)

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OpenNavBarRoute -> launch {
                navManager.navigate(
                    NavCommand.OpenNavBarRoute(
                        route = event.route,
                        saveState = !event.isSelected,
                        restoreState = !event.isSelected,
                    ),
                )
            }
        }
    }
}
