package com.featuremodule.template

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.featuremodule.core.ui.theme.AppTheme
import com.featuremodule.template.ui.AppContent
import com.featuremodule.template.ui.ThemeState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val isLoaded = MutableStateFlow(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        super.onCreate(savedInstanceState)

        // Status and navigation bars can be adjusted here
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT))

        setContent {
            var theme by remember { mutableStateOf(ThemeState()) }
            AppTheme(
                colorsLight = theme.colorsLight,
                colorsDark = theme.colorsDark,
                themeStyle = theme.themeStyle,
            ) {
                AppContent(
                    updateLoadedState = { isLoaded.value = it },
                    updateTheme = { theme = it },
                )
            }
        }
    }

    // Should go before onCreate
    private fun setupSplashScreen() {
        val splash = installSplashScreen()

        splash.setKeepOnScreenCondition {
            // Remove splash when viewmodel "loads"
            !isLoaded.value
        }

        splash.setOnExitAnimationListener { splashScreenView ->
            val scaleAnimator = AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(
                        splashScreenView.view,
                        View.SCALE_X,
                        SPLASH_SCALE_FROM,
                        SPLASH_SCALE_TO,
                    ),
                    ObjectAnimator.ofFloat(
                        splashScreenView.view,
                        View.SCALE_Y,
                        SPLASH_SCALE_FROM,
                        SPLASH_SCALE_TO,
                    ),
                )
                duration = SPLASH_SCALE_LENGTH
                interpolator = AccelerateInterpolator()
            }
            val alphaAnimator = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.ALPHA,
                SPLASH_ALPHA_FROM,
                SPLASH_ALPHA_TO,
            ).apply {
                duration = SPLASH_ALPHA_LENGTH
                interpolator = AccelerateInterpolator()
                doOnEnd { splashScreenView.remove() }
            }

            // was easier to set it like that instead of building single Animator
            scaleAnimator.doOnEnd { alphaAnimator.start() }
            scaleAnimator.start()
        }
    }

    companion object {
        // Scale and lengths were picked by eye and feel, can be adjusted as needed
        private const val SPLASH_SCALE_FROM = 1f
        private const val SPLASH_SCALE_TO = 50f
        private const val SPLASH_SCALE_LENGTH = 350L
        private const val SPLASH_ALPHA_FROM = 1f
        private const val SPLASH_ALPHA_TO = 0f
        private const val SPLASH_ALPHA_LENGTH = 200L
    }
}
