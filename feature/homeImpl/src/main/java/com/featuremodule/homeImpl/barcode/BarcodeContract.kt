package com.featuremodule.homeImpl.barcode

import com.featuremodule.core.ui.UiEvent
import com.featuremodule.core.ui.UiState
import com.google.mlkit.vision.barcode.common.Barcode

internal class State : UiState

internal sealed interface Event : UiEvent {
    data object PopBack : Event
    data class BarcodeReceived(val barcode: Barcode) : Event
}
