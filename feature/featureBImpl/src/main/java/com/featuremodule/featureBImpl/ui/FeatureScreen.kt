package com.featuremodule.featureBImpl.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.featuremodule.featureBApi.FeatureBDestination

@Composable
internal fun FeatureScreen(@Suppress("UNUSED_PARAMETER") viewModel: FeatureVM = hiltViewModel()) {
    Text(FeatureBDestination.ROUTE)
}
