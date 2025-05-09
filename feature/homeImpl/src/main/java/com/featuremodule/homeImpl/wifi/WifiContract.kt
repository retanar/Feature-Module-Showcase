package com.featuremodule.homeImpl.wifi

import android.net.NetworkRequest
import android.net.wifi.ScanResult
import android.net.wifi.WifiNetworkSuggestion
import com.featuremodule.core.ui.UiEvent
import com.featuremodule.core.ui.UiState

internal data class State(
    val wifiNetworks: List<NetworkState> = emptyList(),
    val wifiToConnect: NetworkRequest? = null,
    val wifiSuggestions: ArrayList<WifiNetworkSuggestion>? = null,
    val isLocationEnabled: Boolean = true,
    val isWifiEnabled: Boolean = true,
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
    data class SaveWifi(val network: NetworkState) : Event
    data class ConnectWifi(val network: NetworkState) : Event
    data object ClearWifiEvents : Event
    data class UpdateLocationEnabled(val enabled: Boolean) : Event
    data class UpdateWifiEnabled(val enabled: Boolean) : Event
    data object PopBack : Event
}
