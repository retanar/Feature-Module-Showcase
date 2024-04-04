package com.featuremodule.feature_a_impl

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
        val savedArgNum by backStackEntry.savedStateHandle
            .getStateFlow(FeatureADestination.ARG_NUM, 0)
            .collectAsState()
        remember(argNum, savedArgNum) {
            Log.d("FeatureA", "argNum = $argNum; savedArgNum = $savedArgNum")
        }

        FeatureScreen(route = backStackEntry.destination.route, num = argNum)
    }
}
