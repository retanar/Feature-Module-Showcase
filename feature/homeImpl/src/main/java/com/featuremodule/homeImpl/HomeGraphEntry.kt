package com.featuremodule.homeImpl

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.featuremodule.core.navigation.HIDE_NAV_BAR
import com.featuremodule.homeApi.HomeDestination
import com.featuremodule.homeImpl.camera.TakePhotoScreen
import com.featuremodule.homeImpl.exoplayer.ExoplayerScreen
import com.featuremodule.homeImpl.imageUpload.ImageUploadScreen
import com.featuremodule.homeImpl.ui.HomeScreen

fun NavGraphBuilder.registerHome() {
    composable(HomeDestination.ROUTE) { backStackEntry ->
        HomeScreen(route = backStackEntry.destination.route)
    }

    composable(InternalRoutes.ExoplayerDestination.ROUTE) {
        ExoplayerScreen()
    }

    composable(InternalRoutes.ImageUploadDestination.ROUTE) { backStack ->
        val bitmap by backStack.savedStateHandle
            .getStateFlow<Bitmap?>(InternalRoutes.ImageUploadDestination.BITMAP_POP_ARG, null)
            .collectAsStateWithLifecycle()
        ImageUploadScreen(returnedBitmap = bitmap)
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
        const val BITMAP_POP_ARG = "bitmap"

        fun constructRoute() = ROUTE
    }

    object TakePhotoDestination {
        const val ROUTE = HIDE_NAV_BAR + "take_photo"

        fun constructRoute() = ROUTE
    }
}
