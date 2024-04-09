package com.featuremodule.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharedFlow

/**
 * Common usage is:
 * ```
 *  sharedFlow.CollectWithLifecycle {
 *      doSomething(it)
 *  }
 * ```
 */
@Composable
fun <T> SharedFlow<T>.CollectWithLifecycle(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    collector: FlowCollector<T>,
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(lifecycle) {
        lifecycle.repeatOnLifecycle(state) {
            collect(collector)
        }
    }
}
