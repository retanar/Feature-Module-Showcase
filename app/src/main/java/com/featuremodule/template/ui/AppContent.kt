package com.featuremodule.template.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.featuremodule.core.navigation.NavCommand

@Composable
internal fun AppContent(
    viewModel: MainVM = hiltViewModel(),
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val state by viewModel.state.collectAsState()

    LaunchedEffect(lifecycle, state.commands) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            state.commands.collect {
                navController.handleCommand(it)
            }
        }
    }

    Scaffold(
        bottomBar = {
            AppNavBar(
                openNavBarRoute = { viewModel.postEvent(Event.OpenNavBarRoute(it)) },
                currentDestination = backStackEntry?.destination
            )
        },
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

// Cannot be easily moved to :core due to dependency on navigation library
private fun NavHostController.handleCommand(command: NavCommand) {
    when (command) {
        is NavCommand.Forward -> navigate(command.route)
        NavCommand.PopBack -> popBackStack()
        is NavCommand.PopBackWithArguments<*> -> {
            command.args.forEach { (key, value) ->
                previousBackStackEntry?.savedStateHandle?.set(key, value)
                popBackStack()
            }
        }

        is NavCommand.OpenNavBarRoute -> {
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
