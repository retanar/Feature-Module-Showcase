package com.featuremodule.homeImpl.theming

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.featuremodule.core.ui.theme.ColorsDark
import com.featuremodule.core.ui.theme.ColorsLight

@Composable
internal fun ChooseThemeScreen(viewModel: ChooseThemeVM = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Light themes",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(all = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(ColorsLight.entries) {
                ThemeRadioButton(
                    name = it.name,
                    colorScheme = it.scheme,
                    isSelected = state.previewTheme.colorsLight == it,
                    onClick = { viewModel.postEvent(Event.PreviewLightTheme(it)) },
                )
            }
        }
        ThemePreview(state.previewTheme.colorsLight.scheme)

        Spacer(Modifier.height(24.dp))
        Text(
            text = "Dark themes",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(all = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(ColorsDark.entries) {
                ThemeRadioButton(
                    name = it.name,
                    colorScheme = it.scheme,
                    isSelected = state.previewTheme.colorsDark == it,
                    onClick = { viewModel.postEvent(Event.PreviewDarkTheme(it)) },
                )
            }
        }
        ThemePreview(state.previewTheme.colorsDark.scheme)
    }
}

@Composable
private fun ThemeRadioButton(
    name: String,
    colorScheme: ColorScheme,
    isSelected: Boolean,
    onClick: () -> Unit,
) = Card(modifier = Modifier.clickable { onClick() }) {
    Column(
        modifier = Modifier.padding(all = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(name)
        Row(modifier = Modifier.size(height = 20.dp, width = 40.dp)) {
            Box(
                modifier = Modifier
                    .background(
                        color = colorScheme.primary,
                        shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp),
                    )
                    .fillMaxHeight()
                    .weight(1f),
            )
            Box(
                modifier = Modifier
                    .background(color = colorScheme.secondary)
                    .fillMaxHeight()
                    .weight(1f),
            )
            Box(
                modifier = Modifier
                    .background(
                        color = colorScheme.tertiary,
                        shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp),
                    )
                    .fillMaxHeight()
                    .weight(1f),
            )
        }
        RadioButton(
            selected = isSelected,
            onClick = null,
        )
    }
}

@Composable
private fun ThemePreview(colorScheme: ColorScheme) {
    MaterialTheme(colorScheme = colorScheme) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.large,
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.large,
                )
                .padding(all = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text("Theme Preview")
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Button(onClick = {}) { Text("Button") }
                OutlinedButton(onClick = {}) { Text("Button") }
                TextButton(onClick = {}) { Text("Button") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Card(onClick = {}) {
                    Text(text = "Card", modifier = Modifier.padding(12.dp))
                }
                OutlinedCard(onClick = {}) {
                    Text(text = "Card", modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}
