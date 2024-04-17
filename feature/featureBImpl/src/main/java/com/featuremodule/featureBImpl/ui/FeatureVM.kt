package com.featuremodule.featureBImpl.ui

import android.util.Log
import com.featuremodule.core.ui.BaseVM
import com.featuremodule.data.floof.FoxRepository
import com.featuremodule.data.floof.FoxResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@HiltViewModel
internal class FeatureVM @Inject constructor(
    private val foxRepository: FoxRepository,
) : BaseVM<State, Event>() {
    override fun initialState() = State.Loading

    init {
        launch {
            val result = List(size = 10) { _ ->
                foxRepository.getRandomFox().getOrElse { throwable ->
                    Log.e(tag, throwable.message.orEmpty())
                    FoxResponse()
                }.image.orEmpty()
            }.toImmutableList()
            Log.d(tag, "$result")

            setState {
                if (this is State.Success) {
                    copy(result)
                } else {
                    State.Success(result)
                }
            }
        }
    }

    override fun handleEvent(event: Event) {
        TODO()
    }
}
