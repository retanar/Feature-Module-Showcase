package com.featuremodule.template.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.featuremodule.core.navigation.NavBarItems
import com.featuremodule.featureAApi.FeatureADestination
import com.featuremodule.featureAImpl.registerFeatureA
import com.featuremodule.featureBApi.FeatureBDestination
import com.featuremodule.featureBImpl.registerFeatureB
import com.featuremodule.homeApi.HomeDestination
import com.featuremodule.homeImpl.registerHome

/**
 * Each [NavBarItems] entry should have it's own navigation block.
 *
 * New routes should be added in respective navigation blocks for proper [AppNavBar] item selection,
 * if the selection logic relies on NavBackStackEntry.
 * Placing routes outside of any navigation block, will not highlight any item.
 */
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

        navigation(
            startDestination = FeatureBDestination.ROUTE,
            route = NavBarItems.FeatureB.graphRoute,
        ) {
            registerFeatureB()
        }
    }
}
