package com.featuremodule.homeImpl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.featuremodule.core.navigation.HIDE_NAV_BAR
import com.featuremodule.homeApi.HomeDestination
import com.featuremodule.homeImpl.camera.ImageUploadScreen
import com.featuremodule.homeImpl.camera.TakePhotoScreen
import com.featuremodule.homeImpl.exoplayer.ExoplayerScreen
import com.featuremodule.homeImpl.ui.HomeScreen

fun NavGraphBuilder.registerHome() {
    composable(HomeDestination.ROUTE) { backStackEntry ->
        HomeScreen(route = backStackEntry.destination.route)
    }

    composable(InternalRoutes.ExoplayerDestination.ROUTE) {
        ExoplayerScreen()
    }

    composable(InternalRoutes.ImageUploadDestination.ROUTE) {
        ImageUploadScreen()
    }

    composable(InternalRoutes.TakePhotoDestination.ROUTE) {
        TakePhotoScreen()
    }
}

internal class InternalRoutes {
    object ExoplayerDestination {
        const val ROUTE = HIDE_NAV_BAR + "exoplayer"

        fun constructRoute() = ROUTE
    }

    object ImageUploadDestination {
        const val ROUTE = "image_upload"

        fun constructRoute() = ROUTE
    }

    object TakePhotoDestination {
        const val ROUTE = "take_photo"

        fun constructRoute() = ROUTE
    }
}
