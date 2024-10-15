package com.featuremodule.homeImpl.verification

import com.featuremodule.core.ui.UiEvent
import com.featuremodule.core.ui.UiState

internal data class State(
    val placeholder: String = "",
) : UiState

internal sealed interface Event : UiEvent {
    data class CheckCode(val code: String) : Event
}
