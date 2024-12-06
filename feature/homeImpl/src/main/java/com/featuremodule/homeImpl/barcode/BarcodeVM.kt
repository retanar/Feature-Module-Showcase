package com.featuremodule.homeImpl.barcode

import com.featuremodule.core.navigation.NavCommand
import com.featuremodule.core.navigation.NavManager
import com.featuremodule.core.ui.BaseVM
import com.featuremodule.homeImpl.InternalRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class BarcodeVM @Inject constructor(
    private val navManager: NavManager,
) : BaseVM<State, Event>() {
    override fun initialState() = State()

    override fun handleEvent(event: Event) {
        when (event) {
            Event.PopBack -> launch { navManager.navigate(NavCommand.PopBack) }

            is Event.BarcodeReceived -> launch {
                navManager.navigate(
                    NavCommand.Forward(
                        InternalRoutes.BarcodeResultDestination.constructRoute(
                            event.barcode.rawValue.toString(),
                        ),
                    ),
                )
            }
        }
    }
}
