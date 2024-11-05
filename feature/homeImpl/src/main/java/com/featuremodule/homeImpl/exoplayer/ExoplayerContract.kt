package com.featuremodule.homeImpl.exoplayer

import androidx.media3.exoplayer.ExoPlayer
import com.featuremodule.core.ui.UiEvent
import com.featuremodule.core.ui.UiState

internal data class State(
    val exoplayer: ExoPlayer,
    val overlayState: OverlayState = OverlayState(),
) : UiState

internal data class OverlayState(
    val showPlayButton: Boolean = false,
    val title: String = "",
    val contentPosition: Long = 0,
    val contentDuration: Long = 0,
)

internal sealed interface Event : UiEvent {
    data object OnPlayPauseClick : Event
}
