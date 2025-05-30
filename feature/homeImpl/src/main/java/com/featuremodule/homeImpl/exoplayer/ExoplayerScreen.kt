package com.featuremodule.homeImpl.exoplayer

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.featuremodule.core.util.getActivity

@Composable
internal fun ExoplayerScreen(viewModel: ExoplayerVM = hiltViewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Hide system bars just for this dialog
    DisposableEffect(Unit) {
        val window = context.getActivity()?.window ?: return@DisposableEffect onDispose {}
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        insetsController.apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            insetsController.apply {
                show(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            }
        }
    }

    ExoplayerScreen(
        state = state,
        postEvent = viewModel::postEvent,
    )
}

@OptIn(UnstableApi::class)
@Composable
private fun ExoplayerScreen(state: State, postEvent: (Event) -> Unit) {
    var overlayVisibility by rememberSaveable { mutableStateOf(true) }
    var videoSize by remember { mutableStateOf(IntSize(0, 0)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            // Indication is not needed and it does not look good
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                overlayVisibility = !overlayVisibility
            },
        contentAlignment = Alignment.Center,
    ) {
        AndroidView(
            factory = { viewContext ->
                PlayerView(viewContext).apply {
                    useController = false
                    setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                    background = context.getDrawable(android.R.color.black)
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

                    player = state.exoplayer
                }
            },
            modifier = Modifier.onSizeChanged { intSize ->
                videoSize = intSize
            },
        )

        Overlay(
            state = state.overlayState,
            isVisible = overlayVisibility,
            onPlayPauseClick = { postEvent(Event.OnPlayPauseClick) },
            onSeek = { postEvent(Event.OnSeekFinished(it)) },
            modifier = Modifier.size(
                with(LocalDensity.current) {
                    DpSize(
                        width = videoSize.width.toDp(),
                        height = videoSize.height.toDp(),
                    )
                },
            ),
        )
    }
}
