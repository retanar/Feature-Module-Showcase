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
import javax.inject.Inject

@HiltViewModel
internal class ExoplayerVM @Inject constructor(
    private val exoplayer: ExoPlayer,
) : BaseVM<State, Event>() {
    private val playerEventListener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            if (events.containsAny(
                    Player.EVENT_PLAY_WHEN_READY_CHANGED,
                    Player.EVENT_PLAYBACK_STATE_CHANGED,
                    Player.EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED,
                )
            ) {
                setState { copy(showPlayButton = Util.shouldShowPlayButton(exoplayer)) }
            }
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

    override fun initialState() = State(
        exoplayer = exoplayer,
        title = "fumo_sail",
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
}
