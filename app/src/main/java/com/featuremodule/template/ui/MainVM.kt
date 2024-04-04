package com.featuremodule.template.ui

import com.featuremodule.core.navigation.NavigationCommand
import com.featuremodule.core.navigation.NavigationManager
import com.featuremodule.core.ui.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class MainVM @Inject constructor(
    private val navigationManager: NavigationManager,
) : BaseVM<State, Event>() {
    override fun initialState() = State(navigationManager.commands)

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OpenNavBarRoute -> navigationManager.navigate(
                NavigationCommand.OpenNavBarRoute(event.route)
            )
        }
    }
}