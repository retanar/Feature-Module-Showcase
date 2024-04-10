package com.featuremodule.featureAImpl.ui

import com.featuremodule.core.ui.UiEvent
import com.featuremodule.core.ui.UiState

internal data class State(
    val argNum: Int = 0,
) : UiState

internal sealed interface Event : UiEvent
