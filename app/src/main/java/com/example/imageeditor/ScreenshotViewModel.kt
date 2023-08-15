package com.example.imageeditor

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScreenshotViewModel: ViewModel() {

    private val _boxesOnScreen = MutableStateFlow(listOf<Box>())
    private val _dataToSave = MutableStateFlow(listOf<Box>())

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
        _dataToSave.value = currentList
        _boxesOnScreen.value.forEach {
            Log.i("TEST current list on add", it.id.toString())
        }
    }

    fun updateBox(id: Int, type: BoxType, width: Dp, height: Dp, offset: Offset) {
        // Update the values in the data needed to save the image
        val dataToSaveList = _dataToSave.value.toMutableList()
        dataToSaveList.removeIf { it.id == id }
        dataToSaveList.add(
            Box(
                id = id,
                width = width,
                height = height,
                offset = offset,
                type = type
            )
        )
        _dataToSave.value = dataToSaveList
    }

    fun clear() {
        _boxesOnScreen.value = mutableListOf()
        _dataToSave.value = mutableListOf()
    }

}