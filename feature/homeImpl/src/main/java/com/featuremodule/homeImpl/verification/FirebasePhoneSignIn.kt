package com.featuremodule.homeImpl.verification

import android.util.Log
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

internal fun firebasePhoneSignIn(cred: PhoneAuthCredential, onSuccess: () -> Unit) {
    Firebase.auth
        .signInWithCredential(cred)
        .addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    Log.d("Firebase", "Successful auth")
                    onSuccess()
                }

                task.exception is FirebaseAuthInvalidCredentialsException -> {
                    Log.w("Firebase", "Invalid code")
                }

                else -> {
                    Log.w("Firebase", "Unsuccessful auth", task.exception)
                }
            }
        }
}
