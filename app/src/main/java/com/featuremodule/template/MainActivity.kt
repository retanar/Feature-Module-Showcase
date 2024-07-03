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
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.featuremodule.core.ui.theme.AppTheme
import com.featuremodule.template.ui.AppContent
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
            AppTheme {
                AppContent(updateLoadedState = { isLoaded.value = it })
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
            val rotateAnimator = AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(
                        splashScreenView.view,
                        View.SCALE_X,
                        1f,
                        100f,
                    ),
                    ObjectAnimator.ofFloat(
                        splashScreenView.view,
                        View.SCALE_Y,
                        1f,
                        100f,
                    ),
                )
                duration = 400
                interpolator = AccelerateInterpolator()
            }
            val alphaAnimator = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.ALPHA,
                1f,
                0f,
            ).apply {
                duration = 200
                interpolator = AccelerateInterpolator()
                doOnEnd { splashScreenView.remove() }
            }

            // was easier to set it like that instead of building single Animator
            rotateAnimator.doOnEnd { alphaAnimator.start() }
            rotateAnimator.start()
        }
    }
}
