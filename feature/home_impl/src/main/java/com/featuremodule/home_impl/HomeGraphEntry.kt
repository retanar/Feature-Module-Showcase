package com.featuremodule.home_impl

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.featuremodule.home_api.HomeDestination

fun NavGraphBuilder.registerHome() {
    composable(HomeDestination.route) { backStackEntry ->
        Text(text = backStackEntry.destination.route ?: "null")
    }
}
