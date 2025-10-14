package com.bettor.medlinkduo.core.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/** 스크린리더 설명/라벨을 포함한 클릭 액션 제공 */
fun Modifier.a11yClickable(
    desc: String,
    label: String,
    onClick: () -> Unit,
): Modifier =
    semantics {
        contentDescription = desc
        onClick(label = label) {
            onClick()
            true
        }
    }

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.a11yGestures(
    onDoubleTap: (() -> Unit)? = null,
    onLongPress: (() -> Unit)? = null,
): Modifier =
    pointerInput(onDoubleTap, onLongPress) {
        detectTapGestures(
            onDoubleTap = { onDoubleTap?.invoke() },
            onLongPress = { onLongPress?.invoke() },
        )
    }

/** 최소 터치 타깃(48dp) 보장 */
fun Modifier.minTouchTarget(): Modifier = then(Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp))
