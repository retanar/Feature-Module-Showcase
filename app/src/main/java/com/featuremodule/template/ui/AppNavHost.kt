package com.featuremodule.template.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
internal fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = NavBarItems.Home.route,
        modifier = modifier
    ) {
        // TODO: make a proper nav host
        NavBarItems.entries.forEach { item ->
            composable(item.route) {
                Text(
                    text = it.destination.route ?: "null"
                )
            }
        }
    }
}
