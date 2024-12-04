package com.featuremodule.homeImpl.camera

import android.graphics.Bitmap
import android.graphics.Matrix
import com.featuremodule.core.navigation.NavCommand
import com.featuremodule.core.navigation.NavManager
import com.featuremodule.core.ui.BaseVM
import com.featuremodule.homeImpl.InternalRoutes.ImageUploadDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TakePhotoVM @Inject constructor(
    private val navManager: NavManager,
) : BaseVM<State, Event>() {
    override fun initialState() = State()

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.CaptureSuccess -> launch {
                val rotatedBitmap = rotateBitmap(event.bitmap, event.rotation)
                navManager.navigate(
                    NavCommand.PopBackWithArguments(
                        mapOf(ImageUploadDestination.BITMAP_POP_ARG to rotatedBitmap),
                    ),
                )
            }
        }
    }

    // Because image is not rotated by default, it only has rotation value in EXIF
    private fun rotateBitmap(bitmap: Bitmap, rotation: Int): Bitmap {
        val matrix = Matrix().apply {
            postRotate(rotation.toFloat())
        }
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true,
        )
    }
}
