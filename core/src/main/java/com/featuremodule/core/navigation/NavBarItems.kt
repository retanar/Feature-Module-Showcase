package com.featuremodule.core.navigation

import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.featuremodule.core.R

enum class NavBarItems(
    val graphRoute: String,
    val icon: @Composable () -> Unit,
    @StringRes
    val label: Int,
) {
    Home(
        graphRoute = "home_graph",
        icon = {
            Icon(
                painter = painterResource(R.drawable.home_navbar),
                contentDescription = stringResource(R.string.home_label),
            )
        },
        label = R.string.home_label,
    ),
    FeatureA(
        graphRoute = "feature_graph",
        icon = {
            Icon(
                painter = painterResource(R.drawable.category_navbar),
                contentDescription = stringResource(R.string.feature_label),
            )
        },
        label = R.string.feature_label,
    ),
    FeatureB(
        graphRoute = "foxes_graph",
        icon = {
            Icon(
                painter = painterResource(R.drawable.fox_navbar),
                contentDescription = stringResource(R.string.foxes_label),
            )
        },
        label = R.string.foxes_label,
    ),
}
