package com.featuremodule.homeImpl.imageUpload

import android.graphics.Bitmap
import android.net.Uri
import com.featuremodule.core.ui.UiEvent
import com.featuremodule.core.ui.UiState

internal data class State(
    val image: Any? = null,
) : UiState

internal sealed interface Event : UiEvent {
    data class PhotoTaken(val bitmap: Bitmap) : Event
    data class ImagePicked(val uri: Uri) : Event
    data object OpenInAppCamera : Event
}
