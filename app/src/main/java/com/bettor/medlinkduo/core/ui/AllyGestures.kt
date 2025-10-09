package com.bettor.medlinkduo.core.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.a11yReReadGesture(
    onDoubleTap: () -> Unit,
    onLongPress: () -> Unit,
) = pointerInput(Unit) {
    detectTapGestures(
        onDoubleTap = { onDoubleTap() },
        onLongPress = { onLongPress() },
    )
}
