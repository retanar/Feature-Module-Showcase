package com.featuremodule.homeImpl.exoplayer

import android.content.ContentResolver
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import com.featuremodule.core.ui.BaseVM
import com.featuremodule.homeImpl.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import javax.inject.Inject

@HiltViewModel
internal class ExoplayerVM @Inject constructor(
    private val exoplayer: ExoPlayer,
) : BaseVM<State, Event>() {
    private var progressUpdateJob: Job? = null

    private val playerEventListener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            var updatedState = state.value.overlayState

            if (events.containsAny(
                    Player.EVENT_PLAY_WHEN_READY_CHANGED,
                    Player.EVENT_PLAYBACK_STATE_CHANGED,
                    Player.EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED,
                )
            ) {
                updatedState = updatedState.copy(
                    showPlayButton = Util.shouldShowPlayButton(exoplayer),
                )
            }

            if (events.contains(Player.EVENT_IS_PLAYING_CHANGED)) {
                if (player.isPlaying) {
                    startProgressUpdateJob()
                } else {
                    stopProgressUpdateJob()
                    updatedState = updatedState.copy(
                        contentPosition = player.currentPosition,
                        contentDuration = player.contentDuration.coerceAtLeast(0L),
                    )
                }
            }


            setState { copy(overlayState = updatedState) }
        }
    }

    init {
        with(exoplayer) {
            setMediaItem(
                MediaItem.fromUri(
                    Uri.Builder()
                        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                        .path(R.raw.fumo_sail.toString())
                        .build(),
                ),
            )
            prepare()
            playWhenReady = true

            addListener(playerEventListener)
        }
    }

    @Synchronized
    private fun startProgressUpdateJob() {
        // if job is null, this will not trigger too
        if (progressUpdateJob?.isActive == true) return

        progressUpdateJob = launch {
            while (isActive) {
                setState {
                    val newOverlay = overlayState.copy(
                        contentPosition = exoplayer.currentPosition,
                        contentDuration = exoplayer.contentDuration.coerceAtLeast(0L),
                    )
                    copy(overlayState = newOverlay)
                }
                delay(PROGRESS_UPDATE_DELAY_MS)
            }
        }
    }

    @Synchronized
    private fun stopProgressUpdateJob() {
        progressUpdateJob?.cancel()
        progressUpdateJob = null
    }

    override fun initialState() = State(
        exoplayer = exoplayer,
        overlayState = OverlayState(title = "fumo_sail"),
    )

    override fun handleEvent(event: Event) {
        when (event) {
            Event.OnPlayPauseClick -> {
                Util.handlePlayPauseButtonAction(exoplayer)
            }
        }
    }

    override fun onCleared() {
        exoplayer.removeListener(playerEventListener)
        exoplayer.release()
    }

    companion object {
        const val PROGRESS_UPDATE_DELAY_MS = 500L
    }
}
