package com.featuremodule.template.ui

import com.featuremodule.core.navigation.NavigationCommand
import com.featuremodule.core.ui.UiEvent
import com.featuremodule.core.ui.UiState
import kotlinx.coroutines.flow.SharedFlow

internal data class State(
    val commands: SharedFlow<NavigationCommand>,
) : UiState

internal sealed interface Event : UiEvent {
    data class OpenNavBarRoute(val route: String) : Event
}
