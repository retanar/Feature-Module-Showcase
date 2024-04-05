package com.featuremodule.template.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.featuremodule.core.navigation.NavBarItems
import com.featuremodule.feature_a_api.FeatureADestination
import com.featuremodule.feature_a_impl.registerFeatureA
import com.featuremodule.home_api.HomeDestination
import com.featuremodule.home_impl.registerHome

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
            registerHome()
        }

        navigation(
            startDestination = FeatureADestination.route,
            route = NavBarItems.FeatureA.graphRoute
        ) {
            registerFeatureA()
        }
    }
}
