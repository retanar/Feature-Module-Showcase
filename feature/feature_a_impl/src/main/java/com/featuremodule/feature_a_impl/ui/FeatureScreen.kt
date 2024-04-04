package com.featuremodule.feature_a_impl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun FeatureScreen(
    route: String?,
    num: Int,
    viewModel: FeatureVM = hiltViewModel(),
) {
    Column {
        Text(text = route.toString())
        Text(text = num.toString())
    }
}
