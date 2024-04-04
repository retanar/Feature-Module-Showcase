package com.featuremodule.template.ui

import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.featuremodule.template.R

internal enum class NavBarItems(
    val graphRoute: String,
    val icon: @Composable () -> Unit,
    @StringRes
    val label: Int,
) {
    Home(
        graphRoute = "home_graph",
        icon = {
            Icon(
                painter = painterResource(R.drawable.home),
                contentDescription = "home"
            )
        },
        label = R.string.home_label,
    ),
    FeatureA(
        graphRoute = "feature_a_graph",
        icon = {
            Icon(
                painter = painterResource(R.drawable.home),
                contentDescription = "home"
            )
        },
        label = R.string.feature_a_label
    )
}

@Composable
internal fun AppNavBar(
    openNavBarRoute: (String) -> Unit,
    currentDestination: NavDestination?,
) {
    NavigationBar {
        NavBarItems.entries.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == item.graphRoute }
                    ?: false,
                onClick = {
                    // Consider not navigating to the already selected item if needed
                    openNavBarRoute(item.graphRoute)
                },
                icon = item.icon,
                label = { Text(stringResource(item.label)) }
            )
        }
    }
}
