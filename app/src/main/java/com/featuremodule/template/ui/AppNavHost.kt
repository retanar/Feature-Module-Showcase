package com.featuremodule.template.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.featuremodule.home_api.HomeDestination
import com.featuremodule.home_impl.registerHomeGraph

@Composable
internal fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = NavBarItems.Home.graphRoute,
        modifier = modifier
    ) {
        navigation(
            startDestination = HomeDestination.route,
            route = NavBarItems.Home.graphRoute
        ) {
            registerHomeGraph()
        }
    }
}
