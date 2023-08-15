package com.example.imageeditor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScreenshotViewModel: ViewModel() {

    private val _boxesOnScreen = MutableStateFlow(listOf<Box>())

    val boxesList: StateFlow<List<Box>>
        get() = _boxesOnScreen.asStateFlow()

    fun addNewBox(id: Int, type: BoxType, width: Dp = 100.dp, height: Dp = 30.dp, offset: Offset = Offset(5f, 5f)) {
        val currentList = _boxesOnScreen.value.toMutableList()
        currentList.add(
            Box(
                id = id,
                width = width,
                height = height,
                offset = offset,
                type = type
            )
        )
        _boxesOnScreen.value = currentList
    }

    fun updateBox(id: Int, type: BoxType, width: Dp, height: Dp, offset: Offset) {
        val currentList = _boxesOnScreen.value.toMutableList()
        currentList.removeIf { it.id == id }
        _boxesOnScreen.value = currentList
        addNewBox(id, type, width, height, offset)
    }

    fun clear() {
        _boxesOnScreen.value = mutableListOf()
    }

}