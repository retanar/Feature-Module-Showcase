package com.featuremodule.homeImpl.camera

import com.featuremodule.core.navigation.NavCommand
import com.featuremodule.core.navigation.NavManager
import com.featuremodule.core.ui.BaseVM
import com.featuremodule.homeImpl.InternalRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ImageUploadVM @Inject constructor(
    private val navManager: NavManager,
) : BaseVM<State, Event>() {
    override fun initialState() = State()

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.PhotoTaken -> setState { copy(image = event.bitmap) }
            is Event.ImagePicked -> setState { copy(image = event.uri) }
            Event.OpenInAppCamera -> launch {
                navManager.navigate(
                    NavCommand.Forward(InternalRoutes.TakePhotoDestination.constructRoute()),
                )
            }
        }
    }
}
