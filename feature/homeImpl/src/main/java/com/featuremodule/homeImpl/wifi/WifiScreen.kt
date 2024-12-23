package com.featuremodule.homeImpl.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.ScanResultsCallback
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun WifiScreen(viewModel: WifiVM = hiltViewModel()) {
    val context = LocalContext.current.applicationContext
    val wifiManager = remember { context.getSystemService(Context.WIFI_SERVICE) as WifiManager }
    val connectivityManager =
        remember { context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
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

    val launchAddWifi = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { activityResult ->
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return@rememberLauncherForActivityResult

        val result = activityResult.data
            ?.getIntegerArrayListExtra(Settings.EXTRA_WIFI_NETWORK_RESULT_LIST)
            .orEmpty()

        Log.d(
            "WIFI",
            result.joinToString {
                when (it) {
                    Settings.ADD_WIFI_RESULT_SUCCESS -> "ADD_WIFI_RESULT_SUCCESS"
                    Settings.ADD_WIFI_RESULT_ADD_OR_UPDATE_FAILED -> "ADD_WIFI_RESULT_ADD_OR_UPDATE_FAILED"
                    Settings.ADD_WIFI_RESULT_ALREADY_EXISTS -> "ADD_WIFI_RESULT_ALREADY_EXISTS"
                    else -> "OTHER"
                }
            },
        )

        if (result.any { it == Settings.ADD_WIFI_RESULT_SUCCESS || it == Settings.ADD_WIFI_RESULT_ALREADY_EXISTS }) {
            wifiManager.addNetworkSuggestions(state.wifiSuggestions.orEmpty())
        }
        viewModel.postEvent(Event.ClearWifiEvents)
    }

    LaunchedEffect(state.wifiSuggestions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return@LaunchedEffect

        val suggestions = state.wifiSuggestions
        if (suggestions.isNullOrEmpty()) return@LaunchedEffect

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bundle = Bundle().apply {
                putParcelableArrayList(Settings.EXTRA_WIFI_NETWORK_LIST, suggestions)
            }
            launchAddWifi.launch(Intent(Settings.ACTION_WIFI_ADD_NETWORKS).putExtras(bundle))
        } else {
            // Requires only API Q (29)
            wifiManager.addNetworkSuggestions(suggestions)
            viewModel.postEvent(Event.ClearWifiEvents)
        }
    }

    LaunchedEffect(state.wifiToConnect) {
        if (state.wifiToConnect == null) return@LaunchedEffect

        connectivityManager.requestNetwork(
            state.wifiToConnect!!,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    Log.d("WIFI", "onAvailable")
                }

                override fun onUnavailable() {
                    Log.d("WIFI", "onUnavailable")
                }
            },
        )
        viewModel.postEvent(Event.ClearWifiEvents)
    }

    var clickedWifiItem by remember { mutableStateOf<NetworkState?>(null) }
    LazyColumn {
        if (!isWifiEnabled) {
            item {
                Text(text = "Wifi is not enabled")
            }
        }
        items(items = state.wifiNetworks) {
            WifiNetworkItem(state = it, onClick = { clickedWifiItem = it })
        }
    }

    clickedWifiItem?.let {
        WifiItemMenu(
            onDismiss = { clickedWifiItem = null },
            onSave = { viewModel.postEvent(Event.SaveWifi(it)) },
            onConnect = { viewModel.postEvent(Event.ConnectWifi(it)) },
        )
    }
}

@Composable
private fun WifiNetworkItem(state: NetworkState, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.padding(2.dp)) {
        Row(
            modifier = Modifier.padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.ssid,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (state.channel != -1) {
                    Text(text = "Channel ${state.channel}", fontSize = 14.sp)
                }
            }

            if (state.bandGhz.isNotEmpty() && state.channelWidthMhz != -1) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${state.bandGhz} GHz")
                    Text(text = "${state.channelWidthMhz} MHz", fontSize = 14.sp)
                }
            }

            Text(text = "${state.level}")
        }
    }
}

@Composable
private fun WifiItemMenu(
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    onConnect: () -> Unit,
) {
    @Composable
    fun Entry(text: String, onClick: () -> Unit) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                    onDismiss()
                }
                .padding(all = 16.dp),
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column {
                Entry("Save this network") { onSave() }
                Entry("Connect as IoT") { onConnect() }
            }
        }
    }
}
