package com.featuremodule.featureAImpl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun FeatureScreen(route: String?, viewModel: FeatureVM = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column {
        Text(text = route.toString())
        Text(text = state.argNum.toString())
    }
}
