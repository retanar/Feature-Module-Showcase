package com.featuremodule.homeImpl.verification

import androidx.lifecycle.SavedStateHandle
import com.featuremodule.core.navigation.NavCommand
import com.featuremodule.core.navigation.NavManager
import com.featuremodule.core.ui.BaseVM
import com.featuremodule.homeImpl.InternalDestinations
import com.featuremodule.homeImpl.verification.Event.CheckCode
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class EnterCodeVM @Inject constructor(
    private val navManager: NavManager,
    savedState: SavedStateHandle,
) : BaseVM<State, Event>() {
    private val verificationId = savedState.getStateFlow<String?>(
        InternalDestinations.EnterCode.ARG_VERIFICATION_ID,
        null,
    )

    override fun initialState() = State()

    override fun handleEvent(event: Event) {
        when (event) {
            is CheckCode -> signInByCode(event.code)
        }
    }

    private fun signInByCode(code: String) {
        val verificationId = verificationId.value ?: return

        val cred = PhoneAuthProvider.getCredential(verificationId, code)
        firebasePhoneSignIn(cred) {
            launch {
                navManager.navigate(NavCommand.Forward(InternalDestinations.SignInSuccess.ROUTE))
            }
        }
    }
}
