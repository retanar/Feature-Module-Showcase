package com.featuremodule.homeImpl.camera

import com.featuremodule.core.ui.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ImageUploadVM @Inject constructor(

) : BaseVM<State, Event>() {
    override fun initialState() = State()

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.PhotoTaken -> setState { copy(image = event.bitmap) }
            is Event.ImagePicked -> setState { copy(image = event.uri) }
        }
    }
}
