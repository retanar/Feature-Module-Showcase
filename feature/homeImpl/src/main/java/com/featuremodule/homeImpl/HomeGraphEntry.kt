package com.featuremodule.homeImpl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.featuremodule.homeApi.HomeDestination
import com.featuremodule.homeImpl.ui.HomeScreen
import com.featuremodule.homeImpl.verification.EnterCodeScreen
import com.featuremodule.homeImpl.verification.SignInSuccessScreen

fun NavGraphBuilder.registerHome() {
    composable(HomeDestination.ROUTE) { backStackEntry ->
        HomeScreen(route = backStackEntry.destination.route)
    }
    composable(
        route = InternalDestinations.EnterCode.ROUTE,
        arguments = InternalDestinations.EnterCode.arguments,
    ) {
        EnterCodeScreen()
    }
    composable(InternalDestinations.SignInSuccess.ROUTE) {
        SignInSuccessScreen()
    }
}

sealed interface InternalDestinations {
    data object EnterCode : InternalDestinations {
        const val ARG_VERIFICATION_ID = "argVerificationId"

        const val ROUTE = "enter_code/{$ARG_VERIFICATION_ID}"

        val arguments = listOf(
            navArgument(ARG_VERIFICATION_ID) { type = NavType.StringType },
        )

        fun constructRoute(argVerificationId: String) = "enter_code/$argVerificationId"
    }

    data object SignInSuccess : InternalDestinations {
        const val ROUTE = "sign_in_success"
    }
}

