package com.featuremodule.homeImpl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = route.toString())

        Column(
            modifier = Modifier.width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(24.dp),
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
        }
    }
}
