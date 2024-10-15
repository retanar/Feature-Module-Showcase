package com.featuremodule.homeImpl.ui

import android.util.Log
import com.featuremodule.core.navigation.NavBarItems
import com.featuremodule.core.navigation.NavCommand
import com.featuremodule.core.navigation.NavManager
import com.featuremodule.core.ui.BaseVM
import com.featuremodule.featureAApi.FeatureADestination
import com.featuremodule.homeImpl.InternalDestinations
import com.featuremodule.homeImpl.verification.firebasePhoneSignIn
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
internal class HomeVM @Inject constructor(
    private val navManager: NavManager,
) : BaseVM<State, Event>() {
    override fun initialState() = State

    override fun handleEvent(event: Event) {
        when (event) {
            Event.NavigateToFeatureA -> launch {
                val randomInt = Random.nextInt(until = 10)
                // Using `saveState = false` causes return to Feature A on Home item click
                navManager.navigate(NavCommand.OpenNavBarRoute(NavBarItems.FeatureA.graphRoute))

                navManager.navigate(
                    NavCommand.Forward(FeatureADestination.constructRoute(randomInt)),
                )
            }
        }
    }

    val verificationCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(cred: PhoneAuthCredential) {
            firebasePhoneSignIn(cred) {
                launch {
                    navManager.navigate(
                        NavCommand.Forward(InternalDestinations.SignInSuccess.ROUTE),
                    )
                }
            }
        }

        override fun onCodeSent(verificationId: String, token: ForceResendingToken) {
            launch {
                navManager.navigate(
                    NavCommand.Forward(
                        InternalDestinations.EnterCode.constructRoute(verificationId),
                    ),
                )
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.e("Firebase", "onVerificationFailed", e)
        }
    }
}
