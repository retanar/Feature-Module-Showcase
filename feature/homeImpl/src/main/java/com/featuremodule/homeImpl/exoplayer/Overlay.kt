package com.featuremodule.homeImpl.exoplayer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.featuremodule.homeImpl.R
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun Overlay(
    state: OverlayState,
    isVisible: Boolean,
    onPlayPauseClick: () -> Unit,
    onSeek: (Long) -> Unit,
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
            TopPart(
                title = state.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            )

            CenterPart(
                showPlayButton = state.showPlayButton,
                onPlayPauseClick = onPlayPauseClick,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            BottomPart(
                contentPosition = state.contentPosition,
                contentDuration = state.contentDuration,
                onSeek = onSeek,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp),
            )
        }
    }
}

@Composable
private fun TopPart(title: String, modifier: Modifier = Modifier) = Row(modifier = modifier) {
    Text(
        text = title,
        color = Color.White,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(8.dp),
    )
}

@Composable
private fun CenterPart(
    showPlayButton: Boolean,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Row(modifier = modifier) {
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

@Composable
private fun BottomPart(
    contentPosition: Long,
    contentDuration: Long,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
) {
    var isValueChanging by remember { mutableStateOf(false) }
    var seekPosition by remember { mutableFloatStateOf(0f) }

    Text(
        text = "${formatMs(contentPosition)}/${formatMs(contentDuration)}",
        color = Color.White,
        fontSize = 12.sp,
        modifier = Modifier.padding(end = 8.dp),
    )

    Slider(
        value = if (isValueChanging) {
            seekPosition
        } else {
            contentPosition.toFloat()
        },
        onValueChange = {
            seekPosition = it
            isValueChanging = true
        },
        valueRange = 0f..contentDuration.toFloat(),
        modifier = Modifier.fillMaxWidth(),
        onValueChangeFinished = {
            onSeek(seekPosition.toLong())
            isValueChanging = false
        },
    )
}

private fun formatMs(ms: Long): String {
    return ms.milliseconds.toComponents { hours, minutes, seconds, _ ->
        val secondsString = "%02d".format(seconds)
        if (hours == 0L) {
            "$minutes:$secondsString"
        } else {
            val minutesString = "%02d".format(minutes)
            "$hours:$minutesString:$secondsString"
        }
    }
}
