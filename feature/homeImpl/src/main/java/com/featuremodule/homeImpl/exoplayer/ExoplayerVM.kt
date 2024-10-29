package com.featuremodule.homeImpl.exoplayer

import android.content.ContentResolver
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.featuremodule.core.ui.BaseVM
import com.featuremodule.homeImpl.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ExoplayerVM @Inject constructor(
    private val exoplayer: ExoPlayer,
) : BaseVM<State, Event>() {

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
        }
    }

    override fun initialState() = State(exoplayer = exoplayer)

    override fun handleEvent(event: Event) {
    }

    override fun onCleared() {
        exoplayer.release()
    }
}
