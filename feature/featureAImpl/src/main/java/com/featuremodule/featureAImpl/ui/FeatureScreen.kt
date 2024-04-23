package com.featuremodule.featureAImpl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.featuremodule.core.ui.theme.AppTheme

@Composable
internal fun FeatureScreen(route: String?, viewModel: FeatureVM = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column {
        Text(text = route.toString())
        Text(text = "Random number from home: ${state.argNum}")
        Text(
            text = "Current AppColors.primary value is ${
                AppTheme.colors.primary.toArgb().toUInt().toString(radix = 16)
            }",
            color = AppTheme.colors.primary,
        )
    }
}
