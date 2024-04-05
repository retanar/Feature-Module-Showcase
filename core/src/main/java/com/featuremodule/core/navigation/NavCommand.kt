package com.featuremodule.core.navigation

sealed interface NavCommand {
    data class Forward(val route: String) : NavCommand
    data object PopBack : NavCommand
    data class PopBackWithArguments<T>(val args: Map<String, T>) : NavCommand

    /**
     * [NavBarItems.graphRoute] should be used as [route].
     * Usage with other routes might not work well.
     *
     * Note: setting [saveState] or [restoreState] at false might work in unexpected way.
     * Thorough testing is recommended.
     */
    data class OpenNavBarRoute(
        val route: String,
        val saveState: Boolean = true,
        val restoreState: Boolean = true,
    ) : NavCommand
}
