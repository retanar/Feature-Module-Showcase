package com.featuremodule.featureBImpl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.featuremodule.featureBApi.FeatureBDestination
import com.featuremodule.featureBImpl.ui.FeatureScreen

fun NavGraphBuilder.registerFeatureB() {
    composable(route = FeatureBDestination.ROUTE) {
        FeatureScreen()
    }
}
