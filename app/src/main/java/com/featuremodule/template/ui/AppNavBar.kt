package com.featuremodule.template.ui

import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.featuremodule.template.R

private enum class NavBarItems(
    val destination: String,
    val icon: @Composable () -> Unit,
    @StringRes
    val label: Int,
) {
    Home(
        destination = "home",
        icon = {
            Icon(
                painter = painterResource(R.drawable.home),
                contentDescription = "home"
            )
        },
        label = R.string.home_label,
    ),
}

@Composable
internal fun AppNavBar() {
    NavigationBar {
        NavBarItems.entries.forEach { item ->
            NavigationBarItem(
                selected = true,
                onClick = { /*TODO*/ },
                icon = item.icon,
                label = { Text(stringResource(item.label)) }
            )
        }
    }
}
