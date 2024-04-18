package com.featuremodule.foxFeatureImpl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.featuremodule.foxFeatureApi.FoxFeatureDestination
import com.featuremodule.foxFeatureImpl.ui.FoxScreen

fun NavGraphBuilder.registerFoxFeature() {
    composable(route = FoxFeatureDestination.ROUTE) {
        FoxScreen()
    }
}
