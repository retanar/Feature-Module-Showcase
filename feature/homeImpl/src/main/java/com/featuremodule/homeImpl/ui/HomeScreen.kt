package com.featuremodule.homeImpl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.postEvent(Event.NavigateToFeatureA) },
            ) {
                Text(text = "Pass number")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.postEvent(Event.NavigateToExoplayer) },
            ) {
                Text(text = "Exoplayer")
            }
        }
    }
}
