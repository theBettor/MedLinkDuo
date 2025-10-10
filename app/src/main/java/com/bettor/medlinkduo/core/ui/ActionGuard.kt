package com.bettor.medlinkduo.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ActionGuard(
    private val scope: CoroutineScope,
) {
    private var _acting by mutableStateOf(false)
    val acting: Boolean get() = _acting

    fun launch(block: suspend () -> Unit) {
        if (acting) return
        acting = true
        scope.launch {
            try {
                block()
            } finally {
                acting = false
            }
        }
    }
}

@Composable
fun rememberActionGuard(scope: CoroutineScope): ActionGuard = remember(scope) { ActionGuard(scope) }
