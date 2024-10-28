package com.featuremodule.template.ui

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.featuremodule.core.navigation.HIDE_NAV_BAR
import com.featuremodule.core.navigation.NavBarItems

/**
 * Item selection is currently based on [currentDestination]. However manual selection switching
 * can be implemented if needed, with the selection status being stored in this composable or in
 * ViewModel.
 *
 * @param openNavBarRoute called to navigate to the graph on item click,
 * taking into account if this graph (item) is already selected
 * @param currentDestination destination from NavBackStackEntry,
 * used for checking which graph (item) is selected
 */
@Composable
internal fun AppNavBar(
    openNavBarRoute: (route: String, isSelected: Boolean) -> Unit,
    currentDestination: NavDestination?,
) {
    if (currentDestination?.route.orEmpty().contains(HIDE_NAV_BAR)) return

    NavigationBar {
        NavBarItems.entries.forEach { item ->
            val isSelected = currentDestination?.hierarchy
                ?.any { it.route == item.graphRoute }
                ?: false
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    openNavBarRoute(item.graphRoute, isSelected)
                },
                icon = item.icon,
                label = { Text(stringResource(item.label)) },
            )
        }
    }
}
