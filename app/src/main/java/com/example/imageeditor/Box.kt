package com.example.imageeditor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp

data class Box (
    val id: Int,
    val width: Dp,
    val height: Dp,
    val offset: Offset,
    val type: BoxType
)

enum class BoxType {
    HIGHLIGHT,
    HIDE
}