package com.featuremodule.featureBImpl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.featuremodule.featureBApi.FoxFeatureDestination
import com.featuremodule.featureBImpl.ui.FoxScreen

fun NavGraphBuilder.registerFoxFeature() {
    composable(route = FoxFeatureDestination.ROUTE) {
        FoxScreen()
    }
}
