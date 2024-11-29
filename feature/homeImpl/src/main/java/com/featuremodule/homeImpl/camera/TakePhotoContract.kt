package com.featuremodule.homeImpl.camera

import android.graphics.Bitmap
import com.featuremodule.core.ui.UiEvent
import com.featuremodule.core.ui.UiState

internal class State : UiState

internal sealed interface Event : UiEvent {
    data class CaptureSuccess(val bitmap: Bitmap, val rotation: Int) : Event
}
