package com.featuremodule.featureAApi

import androidx.navigation.NavType
import androidx.navigation.navArgument

object FeatureADestination {
    const val ARG_NUM = "argNum"

    const val route = "feature_a?$ARG_NUM={$ARG_NUM}"

    val arguments = listOf(
        navArgument(ARG_NUM) {
            defaultValue = Int.MIN_VALUE
            type = NavType.IntType
        }
    )

    fun constructRoute(argNum: Int? = null) = buildString {
        append("feature_a")
        if (argNum != null) {
            append("?$ARG_NUM=$argNum")
        }
    }
}
