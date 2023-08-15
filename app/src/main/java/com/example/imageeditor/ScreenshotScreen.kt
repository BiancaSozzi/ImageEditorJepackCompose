package com.example.imageeditor

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.view.drawToBitmap
import kotlin.math.roundToInt


@Composable
fun ScreenshotScreen(viewModel: ScreenshotViewModel) {
    var capturedImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val boxesList: List<Box> by viewModel.boxesList.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Capture Screen Example")

        Row {
            Button(onClick = {
                capturedImageBitmap = captureScreenshot(context)
            }) {
                Text("Capture Screenshot")
            }
        }

        Row {
            Button(onClick = {
                viewModel.addNewBox(boxesList.size.plus(1), BoxType.HIGHLIGHT)
            }) {
                Text("Highlight")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                viewModel.addNewBox(boxesList.size.plus(1), BoxType.HIDE)
            }) {
                Text("Hide")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                capturedImageBitmap = null
                viewModel.clear()
            }) {
                Text("Clear")
            }
        }

        capturedImageBitmap?.let { bitmap ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    bitmap = bitmap,
                    contentDescription = "Captured Screenshot",
                    modifier = Modifier.fillMaxWidth()
                )

                boxesList.forEach {box ->
                    if (box.type == BoxType.HIGHLIGHT) {
                        HighlightContentBox()
                    } else {
                        HideContentBox()
                    }
                }
            }
        }
    }
}

@Composable
fun HideContentBox(){
    RectangleBox(Color.Black)
}

@Composable
fun HighlightContentBox() {
    RectangleBox(Color.Transparent, BorderStroke(2.dp, Color.Red))
}

@Composable
fun RectangleBox(
    background: Color,
    borderStroke: BorderStroke? = null) {

    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }
    var width by remember { mutableStateOf(100.dp) }
    var height by remember { mutableStateOf(30.dp) }

    var size by remember { mutableStateOf(Size.Zero) }

    Box(
        Modifier
            .fillMaxSize()
            .onSizeChanged { size = it.toSize() }
    ) {
        Box(
            Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .size(width = width, height = height)
                .background(background)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        // Current position
                        val original = Offset(offsetX.value, offsetY.value)
                        // New position
                        val summed = original + pan

                        val newWidth = (width * zoom).coerceAtMost(size.width.dp)
                        val newHeight = (height * zoom).coerceAtMost(size.height.dp)

                        // Calculate the maximum allowable offset based on new dimensions
                        val maxX = (size.width - newWidth.toPx()).coerceAtLeast(0f)
                        val maxY = (size.height - newHeight.toPx()).coerceAtLeast(0f)

                        // Create new offset in the limits of the parent
                        val newValue = Offset(
                            x = summed.x.coerceIn(0f, maxX),
                            y = summed.y.coerceIn(0f, maxY)
                        )

                        // Apply new offset
                        offsetX.value = newValue.x
                        offsetY.value = newValue.y
                        width = newWidth
                        height = newHeight
                    }
                }
                .run {
                    if (borderStroke != null) border(borderStroke) else this
                }
        )
    }
}

fun captureScreenshot(context: Context): ImageBitmap {
    val view = (context as ComponentActivity).window.decorView
    return view.drawToBitmap().asImageBitmap()
}

@Preview
@Composable
fun PreviewCaptureScreenScreen() {
    MaterialTheme {
        ScreenshotScreen(ScreenshotViewModel())
    }
}
