package com.featuremodule.core.navigation

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Singleton

@Singleton
class NavManager {
    private val _commands = MutableSharedFlow<NavCommand>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val commands = _commands.asSharedFlow()

    suspend fun navigate(command: NavCommand) {
        _commands.emit(command)
    }
}
