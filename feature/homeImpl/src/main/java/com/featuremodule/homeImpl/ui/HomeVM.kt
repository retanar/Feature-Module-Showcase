package com.featuremodule.homeImpl.ui

import com.featuremodule.core.navigation.NavBarItems
import com.featuremodule.core.navigation.NavCommand
import com.featuremodule.core.navigation.NavManager
import com.featuremodule.core.ui.BaseVM
import com.featuremodule.featureAApi.FeatureADestination
import com.featuremodule.homeImpl.InternalRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
internal class HomeVM @Inject constructor(
    private val navManager: NavManager,
) : BaseVM<State, Event>() {
    override fun initialState() = State

    override fun handleEvent(event: Event) {
        launch {
            when (event) {
                Event.NavigateToFeatureA -> {
                    val randomInt = Random.nextInt(until = 10)
                    // Using `saveState = false` causes return to Feature A on Home item click
                    navManager.navigate(NavCommand.OpenNavBarRoute(NavBarItems.FeatureA.graphRoute))

                    navManager.navigate(
                        NavCommand.Forward(FeatureADestination.constructRoute(randomInt)),
                    )
                }

                Event.NavigateToExoplayer -> navManager.navigate(
                    NavCommand.Forward(InternalRoutes.ExoplayerDestination.constructRoute()),
                )

                Event.NavigateToCamera -> navManager.navigate(
                    NavCommand.Forward(InternalRoutes.ImageUploadDestination.constructRoute()),
                )

                Event.NavigateToBarcode -> navManager.navigate(
                    NavCommand.Forward(InternalRoutes.BarcodeCameraDestination.constructRoute()),
                )

                Event.NavigateToWifi -> navManager.navigate(
                    NavCommand.Forward(InternalRoutes.WifiDestination.constructRoute()),
                )

                Event.NavigateToChooseTheme -> navManager.navigate(
                    NavCommand.Forward(InternalRoutes.ChooseThemeDestination.constructRoute()),
                )
            }
        }
    }
}
