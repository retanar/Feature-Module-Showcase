package com.featuremodule.homeImpl.wifi

import android.net.wifi.ScanResult
import com.featuremodule.core.ui.UiEvent
import com.featuremodule.core.ui.UiState

internal data class State(
    val wifiNetworks: List<NetworkState> = emptyList(),
) : UiState

internal data class NetworkState(
    val ssid: String,
    val bssid: String,
    val bandGhz: String,
    val channel: Int,
    val channelWidthMhz: Int,
    val level: Int,
)

internal sealed interface Event : UiEvent {
    data class WifiResultsScanned(val result: List<ScanResult>) : Event
}
