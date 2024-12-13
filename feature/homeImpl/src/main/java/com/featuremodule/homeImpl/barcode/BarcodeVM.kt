package com.featuremodule.homeImpl.barcode

import com.featuremodule.core.navigation.NavCommand
import com.featuremodule.core.navigation.NavManager
import com.featuremodule.core.ui.BaseVM
import com.featuremodule.homeImpl.InternalRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
internal class BarcodeVM @Inject constructor(
    private val navManager: NavManager,
) : BaseVM<State, Event>() {
    override fun initialState() = State()

    // Throttling due to barcode reader sending multiple results before closing camera
    private val isBarcodeProcessing = AtomicBoolean(false)

    override fun handleEvent(event: Event) {
        when (event) {
            Event.PopBack -> launch { navManager.navigate(NavCommand.PopBack) }

            is Event.BarcodeReceived -> launch {
                if (isBarcodeProcessing.getAndSet(true)) return@launch

                navManager.navigate(
                    NavCommand.Forward(
                        InternalRoutes.BarcodeResultDestination.constructRoute(
                            event.barcode.displayValue.toString(),
                        ),
                    ),
                )

                // Throttle time, can be adjusted as needed
                delay(timeMillis = 5000L)
                isBarcodeProcessing.set(false)
            }
        }
    }
}
