package com.featuremodule.homeImpl.camera

import android.content.Context
import android.util.Rational
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

@Composable
internal fun TakePhotoScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }
    val imageCapture = remember {
        ImageCapture.Builder().build().apply {
            setCropAspectRatio(Rational(1, 1))
        }
    }

    LaunchedEffect(context, lifecycleOwner) {
        setupCameraSettings(
            context = context,
            lifecycleOwner = lifecycleOwner,
            previewView = previewView,
            imageCapture = imageCapture,
        )
    }

    Box {
        AndroidView(
            factory = { previewView },
            Modifier
                .aspectRatio(1f)
                .fillMaxSize()
                .align(Alignment.Center),
        )
    }
}

private fun setupCameraSettings(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    imageCapture: ImageCapture,
) {
    ProcessCameraProvider.getInstance(context).run {
        addListener(
            {
                val cameraSelector = CameraSelector.Builder().build()
                val preview = Preview.Builder().build()
                preview.surfaceProvider = previewView.surfaceProvider
                get().bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
            },
            ContextCompat.getMainExecutor(context),
        )
    }
}
