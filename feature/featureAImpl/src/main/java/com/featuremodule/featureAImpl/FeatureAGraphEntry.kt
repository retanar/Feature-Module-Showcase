package com.featuremodule.featureAImpl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.featuremodule.featureAApi.FeatureADestination
import com.featuremodule.featureAImpl.ui.FeatureScreen

fun NavGraphBuilder.registerFeatureA() {
    composable(
        route = FeatureADestination.route,
        arguments = FeatureADestination.arguments
    ) { backStackEntry ->
        val argNum = backStackEntry.arguments?.getInt(FeatureADestination.ARG_NUM)!!

        FeatureScreen(route = backStackEntry.destination.route, num = argNum)
    }
}
