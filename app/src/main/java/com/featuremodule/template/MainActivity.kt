package com.featuremodule.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.featuremodule.core.ui.theme.AppTheme
import com.featuremodule.template.ui.AppContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Should go immediately after onCreate
        installSplashScreen()

        // Status and navigation bars can be adjusted here
        enableEdgeToEdge()

        setContent {
            AppTheme {
                AppContent()
            }
        }
    }
}
