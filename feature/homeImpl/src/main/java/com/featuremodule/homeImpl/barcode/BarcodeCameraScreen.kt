package com.featuremodule.homeImpl.barcode

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode

@Composable
internal fun BarcodeCameraScreen(viewModel: BarcodeVM = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var cameraViewVisibility by remember { mutableStateOf(false) }
    val launchCameraPermissionRequest =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            if (isGranted) {
                cameraViewVisibility = true
            } else {
                viewModel.postEvent(Event.PopBack)
            }
        }

    val cameraController = remember {
        createCameraController(
            context = context,
            lifecycleOwner = lifecycleOwner,
            onBarcodeReceived = { viewModel.postEvent(Event.BarcodeReceived(it)) },
        )
    }

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FIT_CENTER
            controller = cameraController
        }
    }

    LaunchedEffect(context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launchCameraPermissionRequest.launch(Manifest.permission.CAMERA)
        } else {
            cameraViewVisibility = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .windowInsetsPadding(WindowInsets.navigationBars),
    ) {
        if (cameraViewVisibility) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier
                    .align(Alignment.Center)
                    .aspectRatio(1f)
                    .fillMaxSize(),
            )
        }
    }
}

private fun createCameraController(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    onBarcodeReceived: (Barcode) -> Unit,
): LifecycleCameraController {
    val barcodeScanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_DATA_MATRIX,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_EAN_8,
            )
            .build(),
    )

    return LifecycleCameraController(context).apply {
        bindToLifecycle(lifecycleOwner)
        setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            MlKitAnalyzer(
                listOf(barcodeScanner),
                ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
                ContextCompat.getMainExecutor(context),
            ) { result ->
                result?.getValue(barcodeScanner)?.firstOrNull()?.let {
                    onBarcodeReceived(it)
                }
            },
        )
    }
}
