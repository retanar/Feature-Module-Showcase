package com.featuremodule.homeImpl.camera

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
                navManager.navigate(
                    NavCommand.PopBackWithArguments(
                        mapOf(ImageUploadDestination.BITMAP_POP_ARG to event.bitmap),
                    ),
                )
            }
        }
    }
}
