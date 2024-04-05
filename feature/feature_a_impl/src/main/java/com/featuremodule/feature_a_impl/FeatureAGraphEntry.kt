package com.featuremodule.feature_a_impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.featuremodule.feature_a_api.FeatureADestination
import com.featuremodule.feature_a_impl.ui.FeatureScreen

fun NavGraphBuilder.registerFeatureA() {
    composable(
        route = FeatureADestination.route,
        arguments = FeatureADestination.arguments
    ) { backStackEntry ->
        val argNum = backStackEntry.arguments?.getInt(FeatureADestination.ARG_NUM)!!

        FeatureScreen(route = backStackEntry.destination.route, num = argNum)
    }
}
