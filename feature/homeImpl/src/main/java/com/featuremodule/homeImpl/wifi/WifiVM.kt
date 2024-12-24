package com.featuremodule.homeImpl.wifi

import android.net.MacAddress
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.ScanResult
import android.net.wifi.WifiNetworkSpecifier
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import androidx.annotation.RequiresApi
import com.featuremodule.core.navigation.NavCommand
import com.featuremodule.core.navigation.NavManager
import com.featuremodule.core.ui.BaseVM
import com.featuremodule.core.util.WifiUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class WifiVM @Inject constructor(
    private val navManager: NavManager,
) : BaseVM<State, Event>() {
    override fun initialState() = State()

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.WifiResultsScanned -> setState {
                copy(
                    wifiNetworks = event.result
                        .map { it.toNetworkState() }
                        .sortedByDescending { it.level },
                )
            }

            is Event.SaveWifi -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveAndSuggestWifi(event.network)
            }

            is Event.ConnectWifi -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                connectToIotWifi(event.network)
            }

            Event.ClearWifiEvents -> setState { copy(wifiToConnect = null, wifiSuggestions = null) }
            is Event.UpdateLocationEnabled -> setState { copy(isLocationEnabled = event.enabled) }
            is Event.UpdateWifiEnabled -> setState { copy(isWifiEnabled = event.enabled) }
            Event.PopBack -> launch {
                navManager.navigate(NavCommand.PopBack)
            }
        }
    }

    private fun ScanResult.toNetworkState() = NetworkState(
        ssid = SSID,
        bssid = BSSID,
        bandGhz = toBand(frequency),
        channel = WifiUtils.convertFrequencyMhzToChannelIfSupported(frequency),
        channelWidthMhz = when (channelWidth) {
            ScanResult.CHANNEL_WIDTH_20MHZ -> 20
            ScanResult.CHANNEL_WIDTH_40MHZ -> 40
            ScanResult.CHANNEL_WIDTH_80MHZ,
            ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ -> 80

            ScanResult.CHANNEL_WIDTH_160MHZ -> 160
            ScanResult.CHANNEL_WIDTH_320MHZ -> 320
            else -> -1
        },
        level = level,
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun connectToIotWifi(network: NetworkState) {
        val specifier = WifiNetworkSpecifier.Builder()
            .setSsid(network.ssid)
            .setBssid(MacAddress.fromString(network.bssid))
            .build()
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .setNetworkSpecifier(specifier)
            .build()
        setState { copy(wifiToConnect = request) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveAndSuggestWifi(network: NetworkState) {
        val suggestions = arrayListOf(
            WifiNetworkSuggestion.Builder()
                .setSsid(network.ssid)
                .setBssid(MacAddress.fromString(network.bssid))
                .build(),
        )

        setState { copy(wifiSuggestions = suggestions) }
    }

    private fun toBand(frequency: Int) = when {
        WifiUtils.is24GHz(frequency) -> "2.4"
        WifiUtils.is5GHz(frequency) -> "5"
        WifiUtils.is6GHz(frequency) -> "6"
        WifiUtils.is60GHz(frequency) -> "60"
        else -> ""
    }
}
