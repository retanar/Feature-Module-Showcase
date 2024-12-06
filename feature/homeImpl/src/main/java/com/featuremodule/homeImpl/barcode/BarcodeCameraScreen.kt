package com.featuremodule.homeImpl.barcode

import android.Manifest
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
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode

@Composable
internal fun BarcodeCameraScreen(viewModel: BarcodeVM = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var cameraViewVisibility by remember { mutableStateOf(false) }
    val launchInAppCameraPermissionRequest =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                cameraViewVisibility = true
            } else {
                viewModel.postEvent(Event.PopBack)
            }
        }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launchInAppCameraPermissionRequest.launch(Manifest.permission.CAMERA)
        }
    }

    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_DATA_MATRIX,
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_EAN_8,
        )
        .build()
    val barcodeScanner = BarcodeScanning.getClient(options)

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FIT_CENTER
        }
    }
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                MlKitAnalyzer(
                    listOf(barcodeScanner),
                    ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
                    ContextCompat.getMainExecutor(context),
                ) { result ->
                    result?.getValue(barcodeScanner)?.firstOrNull()?.let {
                        viewModel.postEvent(Event.BarcodeReceived(it))
                    }
                },
            )
            bindToLifecycle(lifecycleOwner)
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    LaunchedEffect(previewView, cameraController) {
        previewView.controller = cameraController
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
                Modifier
                    .align(Alignment.Center)
                    .aspectRatio(1f)
                    .fillMaxSize(),
            )
        }
    }
}
