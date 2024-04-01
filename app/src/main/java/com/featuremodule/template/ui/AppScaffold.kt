package com.featuremodule.template.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun AppScaffold() {
    Scaffold(
        bottomBar = { AppNavBar() },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        AppNavHost(modifier = Modifier.padding(innerPadding))
    }
}