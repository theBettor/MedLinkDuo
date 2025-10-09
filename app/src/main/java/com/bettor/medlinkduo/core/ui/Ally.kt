package com.bettor.medlinkduo.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics

@Composable
fun AnnounceEffect(message: String?) {
    val view = LocalView.current
    LaunchedEffect(message) { if (!message.isNullOrBlank()) view.announceForAccessibility(message) }
}

fun Modifier.liveRegionPolite(): Modifier = this.semantics { liveRegion = LiveRegionMode.Polite }

fun Modifier.a11yClickable(
    desc: String,
    label: String,
    onClick: () -> Unit,
): Modifier =
    this.semantics {
        contentDescription = desc
        onClick(label = label) {
            onClick()
            true
        }
    }
