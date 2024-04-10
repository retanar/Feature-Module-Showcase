package com.featuremodule.template.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.featuremodule.core.navigation.NavBarItems
import com.featuremodule.featureAApi.FeatureADestination
import com.featuremodule.featureAImpl.registerFeatureA
import com.featuremodule.homeApi.HomeDestination
import com.featuremodule.homeImpl.registerHome

@Composable
internal fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = NavBarItems.Home.graphRoute,
        modifier = modifier,
    ) {
        navigation(
            startDestination = HomeDestination.ROUTE,
            route = NavBarItems.Home.graphRoute,
        ) {
            registerHome()
        }

        navigation(
            startDestination = FeatureADestination.ROUTE,
            route = NavBarItems.FeatureA.graphRoute,
        ) {
            registerFeatureA()
        }
    }
}
