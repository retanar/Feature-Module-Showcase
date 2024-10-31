package com.featuremodule.homeImpl.exoplayer

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import com.featuremodule.homeImpl.R

@OptIn(UnstableApi::class)
@Composable
internal fun ExoplayerScreen(viewModel: ExoplayerVM = hiltViewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val exoplayer = state.exoplayer

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

    var overlayVisibility by rememberSaveable { mutableStateOf(false) }

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
                    player = exoplayer
                    useController = false

                    setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                    background = context.getDrawable(android.R.color.black)
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                }
            },
        )

        Overlay(
            title = state.title,
            isVisible = overlayVisibility,
            showPlayButton = state.showPlayButton,
            onPlayPauseClick = { viewModel.postEvent(Event.OnPlayPauseClick) },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun Overlay(
    title: String,
    isVisible: Boolean,
    showPlayButton: Boolean,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Column(
            modifier = modifier.background(Color.Black.copy(alpha = 0.5f)),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // Top
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(8.dp),
                )
            }

            // Center
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                IconButton(
                    onClick = onPlayPauseClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White,
                    ),
                ) {
                    if (showPlayButton) {
                        Icon(painterResource(id = R.drawable.play), contentDescription = null)
                    } else {
                        Icon(painterResource(id = R.drawable.pause), contentDescription = null)
                    }
                }
            }

            // Bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {

            }
        }
    }
}
