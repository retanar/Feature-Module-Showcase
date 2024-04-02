package com.featuremodule.template.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.featuremodule.core.navigation.NavigationCommand
import com.featuremodule.core.navigation.NavigationManager

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun AppContent() {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(lifecycle, NavigationManager.commands) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            NavigationManager.commands.collect {
                navController.handleCommand(it)
            }
        }
    }

    Scaffold(
        bottomBar = { AppNavBar(NavigationManager, backStackEntry?.destination) },
        contentWindowInsets = WindowInsets(0),
        // Remove this and status bar coloring in AppTheme for edge to edge
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier
                .padding(innerPadding)
                // Fixes issues with imePadding and NavBar
                .consumeWindowInsets(innerPadding)
        )
    }
}

private fun NavHostController.handleCommand(command: NavigationCommand) {
    when (command) {
        is NavigationCommand.Forward -> navigate(command.route)
        NavigationCommand.PopBack -> popBackStack()
        is NavigationCommand.PopBackWithArguments<*> -> {
            command.args.forEach { (key, value) ->
                previousBackStackEntry?.savedStateHandle?.set(key, value)
                popBackStack()
            }
        }

        is NavigationCommand.OpenNavBarDestination -> {
            navigate(command.route) {
                popUpTo(graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}
