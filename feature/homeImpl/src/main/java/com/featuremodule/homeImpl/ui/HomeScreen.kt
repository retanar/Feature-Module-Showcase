package com.featuremodule.homeImpl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun HomeScreen(route: String?, viewModel: HomeVM = hiltViewModel()) {
    Column {
        Text(text = route.toString())
        Button(onClick = { viewModel.postEvent(Event.NavigateToFeatureA) }) {
            Text(text = "Pass number")
        }
    }
}
