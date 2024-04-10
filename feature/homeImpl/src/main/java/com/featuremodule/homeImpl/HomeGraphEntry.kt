package com.featuremodule.homeImpl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.featuremodule.homeApi.HomeDestination
import com.featuremodule.homeImpl.ui.HomeScreen

fun NavGraphBuilder.registerHome() {
    composable(HomeDestination.route) { backStackEntry ->
        HomeScreen(route = backStackEntry.destination.route)
    }
}
