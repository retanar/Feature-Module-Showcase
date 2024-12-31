package com.featuremodule.template.ui

import com.featuremodule.core.navigation.NavCommand
import com.featuremodule.core.navigation.NavManager
import com.featuremodule.core.ui.BaseVM
import com.featuremodule.core.ui.theme.ColorsDark
import com.featuremodule.core.ui.theme.ColorsLight
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
        colorsLight = ColorsLight.valueOf(lightTheme ?: ColorsLight.Default.name).scheme,
        colorsDark = ColorsDark.valueOf(darkTheme ?: ColorsDark.Default.name).scheme,
        switchToDarkWithSystem = switchToDarkWithSystem,
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
