package com.featuremodule.home_impl.ui

import com.featuremodule.core.ui.UiEvent
import com.featuremodule.core.ui.UiState

internal data object State : UiState

internal sealed interface Event : UiEvent {
    data object NavigateToFeatureA : Event
}
