package com.featuremodule.homeImpl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.featuremodule.core.navigation.HIDE_NAV_BAR
import com.featuremodule.homeApi.HomeDestination
import com.featuremodule.homeImpl.exoplayer.ExoplayerScreen
import com.featuremodule.homeImpl.ui.HomeScreen

fun NavGraphBuilder.registerHome() {
    composable(HomeDestination.ROUTE) { backStackEntry ->
        HomeScreen(route = backStackEntry.destination.route)
    }

    composable(InternalRoutes.ExoplayerDestination.ROUTE) {
        ExoplayerScreen()
    }
}

internal sealed class InternalRoutes {
    object ExoplayerDestination {
        const val ROUTE = HIDE_NAV_BAR + "exoplayer"

        fun constructRoute() = ROUTE
    }
}
