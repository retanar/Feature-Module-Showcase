package com.featuremodule.homeImpl.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.ScanResultsCallback
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle

@Composable
internal fun WifiScreen(viewModel: WifiVM = hiltViewModel()) {
    val context = LocalContext.current
    val wifiManager = remember { context.getSystemService(WifiManager::class.java) }

    val state by viewModel.state.collectAsStateWithLifecycle()

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val locationManager = remember { context.getSystemService(LocationManager::class.java) }
    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.postEvent(
                Event.UpdateLocationEnabled(
                    LocationManagerCompat.isLocationEnabled(locationManager),
                ),
            )
            viewModel.postEvent(Event.UpdateWifiEnabled(wifiManager.isWifiEnabled))
        }
    }

    fun sendScanResults() {
        try {
            viewModel.postEvent(Event.WifiResultsScanned(wifiManager.scanResults))
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    DisposableWifiScanCallback(
        context = context,
        wifiManager = wifiManager,
        onReceive = ::sendScanResults,
    )

    LaunchedEffect(state.isWifiEnabled, state.isLocationEnabled) {
        sendScanResults()
        // Just location can work
        if (state.isLocationEnabled) {
            wifiManager.startScan()
        }
    }

    AddWifiSuggestion(
        wifiSuggestions = state.wifiSuggestions,
        wifiManager = wifiManager,
        onDone = { viewModel.postEvent(Event.ClearWifiEvents) },
    )

    ConnectToWifi(
        wifiToConnect = state.wifiToConnect,
        context = context,
        onDone = { viewModel.postEvent(Event.ClearWifiEvents) },
    )

    WifiScreen(
        state = state,
        startActivity = { context.startActivity(it) },
        postEvent = viewModel::postEvent,
    )
}

@Composable
private fun WifiScreen(
    state: State,
    startActivity: (Intent) -> Unit,
    postEvent: (Event) -> Unit,
) {
    var clickedWifiItem by remember { mutableStateOf<NetworkState?>(null) }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        if (!state.isWifiEnabled) {
            item {
                Text(text = "Wifi is not enabled", modifier = Modifier.padding(8.dp))
                Button(
                    onClick = { startActivity(Intent(Settings.ACTION_WIFI_SETTINGS)) },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(text = "Enable wifi")
                }
            }
        }

        if (!state.isLocationEnabled) {
            item {
                Text(text = "Location is not enabled", modifier = Modifier.padding(8.dp))
                Button(
                    onClick = { startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(text = "Enable location")
                }
            }
        }

        items(items = state.wifiNetworks) {
            WifiNetworkItem(state = it, onClick = { clickedWifiItem = it })
        }
    }

    clickedWifiItem?.let {
        WifiItemMenu(
            onDismiss = { clickedWifiItem = null },
            onSave = { postEvent(Event.SaveWifi(it)) },
            onConnect = { postEvent(Event.ConnectWifi(it)) },
        )
    }
}

@Composable
private fun WifiNetworkItem(
    state: NetworkState,
    onClick: () -> Unit
) = Card(
    onClick = onClick,
    modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(),
) {
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

@Composable
private fun DisposableWifiScanCallback(
    context: Context,
    wifiManager: WifiManager,
    onReceive: () -> Unit,
) = DisposableEffect(context, wifiManager) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val callback = object : ScanResultsCallback() {
            override fun onScanResultsAvailable() {
                onReceive()
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
                onReceive()
            }
        }
        val intentFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        context.registerReceiver(wifiScanReceiver, intentFilter)

        onDispose {
            context.unregisterReceiver(wifiScanReceiver)
        }
    }
}

@Composable
private fun AddWifiSuggestion(
    wifiSuggestions: ArrayList<WifiNetworkSuggestion>?,
    wifiManager: WifiManager,
    onDone: () -> Unit,
) {
    val launchAddWifi = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { activityResult ->
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return@rememberLauncherForActivityResult

        val result = activityResult.data
            ?.getIntegerArrayListExtra(Settings.EXTRA_WIFI_NETWORK_RESULT_LIST)
            .orEmpty()

        if (result.any {
                it == Settings.ADD_WIFI_RESULT_SUCCESS
                    || it == Settings.ADD_WIFI_RESULT_ALREADY_EXISTS
            }) {
            wifiManager.addNetworkSuggestions(wifiSuggestions.orEmpty())
        }
        onDone()
    }

    LaunchedEffect(wifiSuggestions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return@LaunchedEffect

        if (wifiSuggestions.isNullOrEmpty()) return@LaunchedEffect

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bundle = Bundle()
            bundle.putParcelableArrayList(Settings.EXTRA_WIFI_NETWORK_LIST, wifiSuggestions)
            launchAddWifi.launch(Intent(Settings.ACTION_WIFI_ADD_NETWORKS).putExtras(bundle))
        } else {
            // Requires only API Q (29)
            wifiManager.addNetworkSuggestions(wifiSuggestions)
            onDone()
        }
    }
}

@Composable
private fun ConnectToWifi(
    context: Context,
    wifiToConnect: NetworkRequest?,
    onDone: () -> Unit,
) {
    val connectivityManager = remember { context.getSystemService(ConnectivityManager::class.java) }
    LaunchedEffect(wifiToConnect) {
        if (wifiToConnect == null) return@LaunchedEffect

        connectivityManager.requestNetwork(
            wifiToConnect,
            ConnectivityManager.NetworkCallback(),
        )
        onDone()
    }
}
