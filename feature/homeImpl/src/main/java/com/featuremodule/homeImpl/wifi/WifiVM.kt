package com.featuremodule.homeImpl.wifi

import android.net.wifi.ScanResult
import com.featuremodule.core.ui.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class WifiVM @Inject constructor() : BaseVM<State, Event>() {
    override fun initialState() = State()

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.WifiResultsScanned -> setState {
                copy(wifiNetworks = event.result.map { it.toNetworkState() })
            }
        }
    }

    private fun ScanResult.toNetworkState() = NetworkState(
        ssid = SSID,
        bssid = BSSID,
        bandGhz = "",
        channel = ScanResult.convertFrequencyMhzToChannelIfSupported(frequency),
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
}
