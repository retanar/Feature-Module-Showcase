package com.featuremodule.template.ui

import com.featuremodule.core.navigation.NavCommand
import com.featuremodule.core.navigation.NavManager
import com.featuremodule.core.ui.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class MainVM @Inject constructor(
    private val navManager: NavManager,
) : BaseVM<State, Event>() {
    override fun initialState() = State(navManager.commands)

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OpenNavBarRoute -> launch {
                navManager.navigate(
                    NavCommand.OpenNavBarRoute(
                        route = event.route,
                        saveState = !event.isSelected,
                        restoreState = !event.isSelected
                    )
                )
            }
        }
    }
}