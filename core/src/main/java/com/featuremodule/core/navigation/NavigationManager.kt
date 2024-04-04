package com.featuremodule.core.navigation

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Singleton

@Singleton
class NavigationManager {
    private val _commands = MutableSharedFlow<NavigationCommand>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val commands = _commands.asSharedFlow()

    // Something is needed to not make this suspend but also emit without overflowing.
    // Or make it suspend.
    @Synchronized
    fun navigate(command: NavigationCommand) {
        _commands.tryEmit(command)
    }
}
