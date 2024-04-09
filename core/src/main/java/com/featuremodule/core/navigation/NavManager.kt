package com.featuremodule.core.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Singleton

/**
 * Simplifies navigation by not depending on NavController and defining preset functionality
 * in [NavCommand].
 *
 * Main usage of this class is injection in ViewModels. Only one subscriber should exist in the app,
 * which will execute NavCommands with NavController.
 */
@Singleton
class NavManager {
    private val _commands = MutableSharedFlow<NavCommand>()
    val commands = _commands.asSharedFlow()

    suspend fun navigate(command: NavCommand) {
        _commands.emit(command)
    }
}
