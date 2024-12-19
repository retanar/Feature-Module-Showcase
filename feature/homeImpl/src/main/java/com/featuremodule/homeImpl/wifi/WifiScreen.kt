package com.featuremodule.homeImpl.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.ScanResultsCallback
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun WifiScreen(viewModel: WifiVM = hiltViewModel()) {
    val context = LocalContext.current.applicationContext
    val wifiManager = remember { context.getSystemService(Context.WIFI_SERVICE) as WifiManager }
    val isWifiEnabled by remember { mutableStateOf(wifiManager.isWifiEnabled) }

    val state by viewModel.state.collectAsStateWithLifecycle()

    fun sendScanResults() {
        try {
            viewModel.postEvent(Event.WifiResultsScanned(wifiManager.scanResults))
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    DisposableEffect(context, wifiManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val callback = object : ScanResultsCallback() {
                override fun onScanResultsAvailable() {
                    sendScanResults()
                }
            }
            wifiManager.registerScanResultsCallback(
                ContextCompat.getMainExecutor(context),
                callback,
            )

            onDispose {
                wifiManager.unregisterScanResultsCallback(callback)
            }
        } else {
            val wifiScanReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    sendScanResults()
                }
            }
            val intentFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            context.registerReceiver(wifiScanReceiver, intentFilter)

            onDispose {
                context.unregisterReceiver(wifiScanReceiver)
            }
        }
    }

    LaunchedEffect(isWifiEnabled) {
        sendScanResults()
        if (isWifiEnabled) {
            wifiManager.startScan()
        }
    }

    LazyColumn {
        if (!isWifiEnabled) {
            item {
                Text(text = "Wifi is not enabled")
            }
        }
        items(items = state.wifiNetworks) {
            WifiNetworkItem(state = it)
        }
    }
}

@Composable
private fun WifiNetworkItem(state: NetworkState) {
    Card {
        Row(
            modifier = Modifier.padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = state.ssid, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            }
            Text(text = state.level.toString())
        }
    }
}
