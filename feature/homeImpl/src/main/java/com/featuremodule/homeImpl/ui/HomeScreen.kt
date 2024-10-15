package com.featuremodule.homeImpl.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

@Composable
internal fun HomeScreen(route: String?, viewModel: HomeVM = hiltViewModel()) {
    val activity = LocalContext.current.getActivity()!!
    val auth = remember {
        Firebase.auth.apply {
            useAppLanguage()
        }
    }

    fun verifyPhone(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setActivity(activity)
            .setPhoneNumber(phoneNumber)
            .setTimeout(2, TimeUnit.MINUTES)
            .setCallbacks(viewModel.verificationCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = route.toString())
        Button(onClick = { viewModel.postEvent(Event.NavigateToFeatureA) }) {
            Text(text = "Pass random number to Feature")
        }

        Spacer(modifier = Modifier.height(32.dp))

        var phone by remember { mutableStateOf("") }
        Column {
            Text(text = "Phone number Verification")
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                label = { Text(text = "Phone") },
            )
            Button(
                onClick = { verifyPhone(phone) },
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(text = "Verify")
            }
        }
    }
}

tailrec fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}
