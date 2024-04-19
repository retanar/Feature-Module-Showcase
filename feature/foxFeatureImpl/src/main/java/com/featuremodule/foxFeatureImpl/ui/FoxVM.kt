package com.featuremodule.foxFeatureImpl.ui

import android.util.Log
import com.featuremodule.core.ui.BaseVM
import com.featuremodule.data.floof.FoxRepository
import com.featuremodule.data.floof.FoxResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import javax.inject.Inject

@HiltViewModel
internal class FoxVM @Inject constructor(
    private val foxRepository: FoxRepository,
) : BaseVM<State, Event>() {
    override fun initialState() = State.Loading

    init {
        launch {
            repeat(FOX_COUNT) {
                foxRepository.getRandomFox()
                    .onSuccess(::appendImage)
                    .onFailure { throwable ->
                        Log.e(tag, throwable.message, throwable)
                    }
            }
        }
    }

    private fun appendImage(foxResponse: FoxResponse) {
        val image = foxResponse.image ?: run {
            Log.e(tag, "Image in $foxResponse was null")
            return
        }

        setState {
            return@setState (this as? State.Success)
                ?.copy(images = images.plus(image))
                ?: State.Success(images = persistentListOf(image))
        }
    }

    override fun handleEvent(event: Event) {
        TODO()
    }

    companion object {
        const val FOX_COUNT = 10
    }
}
