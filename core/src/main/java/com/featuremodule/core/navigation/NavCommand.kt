package com.featuremodule.core.navigation

sealed interface NavCommand {
    data class Forward(val route: String) : NavCommand
    data object PopBack : NavCommand
    data class PopBackWithArguments<T>(val args: Map<String, T>) : NavCommand
    data class OpenNavBarRoute(val route: String) : NavCommand
}
