package com.featuremodule.homeImpl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun HomeScreen(route: String?, viewModel: HomeVM = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = route.toString())

        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            @Composable
            fun GenericButton(text: String, onClick: () -> Unit) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onClick,
                ) {
                    Text(text = text)
                }
            }

            GenericButton(text = "Pass number") { viewModel.postEvent(Event.NavigateToFeatureA) }
            GenericButton(text = "Exoplayer") { viewModel.postEvent(Event.NavigateToExoplayer) }
            GenericButton(text = "Camera") { viewModel.postEvent(Event.NavigateToCamera) }
            GenericButton(text = "Barcode") { viewModel.postEvent(Event.NavigateToBarcode) }
            GenericButton(text = "Wifi") { viewModel.postEvent(Event.NavigateToWifi) }
        }
    }
}
