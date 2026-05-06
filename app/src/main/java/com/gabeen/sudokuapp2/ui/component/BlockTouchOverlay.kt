package com.gabeen.sudokuapp2.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput

/* 터치 차단 레이어 */
@Composable
fun BlockTouchOverlay(enabled: Boolean) {
    if (!enabled) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                        event.changes.forEach { it.consume() } // ✅ 여기서 실제 소비
                    }
                }
            }
    )
}