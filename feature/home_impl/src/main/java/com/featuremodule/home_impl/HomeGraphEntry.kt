package com.featuremodule.home_impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.featuremodule.home_api.HomeDestination
import com.featuremodule.home_impl.ui.HomeScreen

fun NavGraphBuilder.registerHome() {
    composable(HomeDestination.route) { backStackEntry ->
        HomeScreen(route = backStackEntry.destination.route)
    }
}
