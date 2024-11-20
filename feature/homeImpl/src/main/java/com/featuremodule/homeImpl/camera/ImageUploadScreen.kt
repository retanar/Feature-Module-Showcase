package com.featuremodule.homeImpl.camera

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun ImageUploadScreen(viewModel: ImageUploadVM = hiltViewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    val launchPhoto = rememberLauncherForActivityResult(TakePicturePreview()) { bitmap ->
        bitmap?.let { viewModel.postEvent(Event.PhotoTaken(it)) }
    }
    val launchImagePicker = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        uri?.let { viewModel.postEvent(Event.ImagePicked(it)) }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.width(200.dp),
        ) {
            GlideImage(
                model = state.image,
                contentDescription = null,
                modifier = Modifier.size(200.dp),
            )

            Button(
                onClick = { launchPhoto.launch() },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Open camera app")
            }

            Button(
                onClick = { launchImagePicker.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Open image picker")
            }
        }
    }
}
