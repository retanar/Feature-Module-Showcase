package com.featuremodule.homeImpl.imageUpload

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
internal fun ImageUploadScreen(
    returnedBitmap: Bitmap?,
    viewModel: ImageUploadVM = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var permissionNotGrantedVisibility by remember { mutableStateOf(false) }
    var cameraNotFoundVisibility by remember { mutableStateOf(false) }

    val launchSystemPhotoTaker = rememberLauncherForActivityResult(TakePicturePreview()) { bitmap ->
        bitmap?.let { viewModel.postEvent(Event.PhotoTaken(it)) }
    }
    val launchImagePicker = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        uri?.let { viewModel.postEvent(Event.ImagePicked(it)) }
    }
    val launchCameraPermissionRequest =
        rememberLauncherForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.postEvent(Event.OpenInAppCamera)
            } else {
                permissionNotGrantedVisibility = true
            }
        }

    LaunchedEffect(returnedBitmap) {
        returnedBitmap?.let {
            viewModel.postEvent(Event.PhotoTaken(it))
        }
    }

    Box {
        ImageUploadScreen(
            state = state,
            launchPhotoTaker = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA,
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    launchCameraPermissionRequest.launch(Manifest.permission.CAMERA)
                } else {
                    try {
                        launchSystemPhotoTaker.launch()
                    } catch (e: ActivityNotFoundException) {
                        cameraNotFoundVisibility = true
                    }
                }
            },
            launchImagePicker = {
                launchImagePicker.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            },
            launchCamera = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA,
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    launchCameraPermissionRequest.launch(Manifest.permission.CAMERA)
                } else {
                    viewModel.postEvent(Event.OpenInAppCamera)
                }
            },
        )

        PermissionNotGrantedDialog(
            isVisible = permissionNotGrantedVisibility,
            onDismiss = { permissionNotGrantedVisibility = false },
        )

        CameraNotFoundDialog(
            isVisible = cameraNotFoundVisibility,
            onDismiss = { cameraNotFoundVisibility = false },
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ImageUploadScreen(
    state: State,
    launchPhotoTaker: () -> Unit,
    launchImagePicker: () -> Unit,
    launchCamera: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState(),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(modifier = Modifier.width(200.dp)) {
            GlideImage(
                model = state.image,
                contentDescription = null,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .size(200.dp),
                contentScale = ContentScale.Crop,
            )

            @Composable
            fun GenericButton(text: String, onClick: () -> Unit) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onClick,
                ) {
                    Text(text = text)
                }
            }

            GenericButton(text = "Open camera app") { launchPhotoTaker() }
            GenericButton(text = "Open image picker") { launchImagePicker() }
            GenericButton(text = "Open in-app camera") { launchCamera() }
        }
    }
}

@Composable
private fun PermissionNotGrantedDialog(isVisible: Boolean, onDismiss: () -> Unit) {
    if (!isVisible) return

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .width(IntrinsicSize.Max),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Permission was not granted",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Close")
                }
            }
        }
    }
}

@Composable
private fun CameraNotFoundDialog(isVisible: Boolean, onDismiss: () -> Unit) {
    if (!isVisible) return

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .width(IntrinsicSize.Max),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Camera app was not found",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Close")
                }
            }
        }
    }
}
