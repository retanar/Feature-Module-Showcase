package com.featuremodule.template.ui

import com.featuremodule.core.navigation.NavCommand
import com.featuremodule.core.ui.UiEvent
import com.featuremodule.core.ui.UiState
import kotlinx.coroutines.flow.SharedFlow

internal data class State(
    val commands: SharedFlow<NavCommand>,
) : UiState

internal sealed interface Event : UiEvent {
    data class OpenNavBarRoute(val route: String, val isSelected: Boolean) : Event
}
