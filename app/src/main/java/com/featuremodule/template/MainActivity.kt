package com.featuremodule.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.featuremodule.core.ui.theme.AppTheme
import com.featuremodule.template.ui.AppContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val isLoaded = MutableStateFlow(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        // Should go before onCreate
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)

        splash.setKeepOnScreenCondition {
            // Remove splash when viewmodel "loads"
            !isLoaded.value
        }

        // Status and navigation bars can be adjusted here
        enableEdgeToEdge()

        setContent {
            AppTheme {
                AppContent(updateLoadedState = { isLoaded.value = it })
            }
        }
    }
}
