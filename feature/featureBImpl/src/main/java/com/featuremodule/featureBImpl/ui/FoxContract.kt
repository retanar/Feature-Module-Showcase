package com.featuremodule.featureBImpl.ui

import com.featuremodule.core.ui.UiEvent
import com.featuremodule.core.ui.UiState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

internal sealed interface State : UiState {
    data object Loading : State

    data class Success(
        val images: PersistentList<String> = persistentListOf(),
    ) : State
}

internal sealed interface Event : UiEvent
