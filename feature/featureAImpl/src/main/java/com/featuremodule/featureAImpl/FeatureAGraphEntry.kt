package com.featuremodule.featureAImpl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.featuremodule.featureAApi.FeatureADestination
import com.featuremodule.featureAImpl.ui.FeatureScreen

fun NavGraphBuilder.registerFeatureA() {
    composable(
        route = FeatureADestination.ROUTE,
        arguments = FeatureADestination.arguments,
    ) { backStackEntry ->
        FeatureScreen(route = backStackEntry.destination.route)
    }
}
