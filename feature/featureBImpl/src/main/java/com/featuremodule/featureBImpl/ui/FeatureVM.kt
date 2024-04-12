package com.featuremodule.featureBImpl.ui

import com.featuremodule.core.ui.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class FeatureVM @Inject constructor() : BaseVM<State, Event>() {
    override fun initialState() = State

    override fun handleEvent(event: Event) {
        TODO()
    }
}
