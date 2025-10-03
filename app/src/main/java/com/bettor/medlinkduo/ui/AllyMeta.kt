package com.bettor.medlinkduo.ui

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 최소 터치 타깃(48dp 이상)
fun Modifier.minTouchTarget(): Modifier =
    this.then(Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp))