package com.featuremodule.feature_a_impl

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.featuremodule.feature_a_api.FeatureADestination

fun NavGraphBuilder.registerFeatureA() {
    composable(route = FeatureADestination.route) {
        Text(text = it.destination.route ?: "null")
    }
}
