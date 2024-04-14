package com.example.todolist.ui.util

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.height
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp

@Composable
fun CollapsingTopAppBar(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit
) {
    val toolbarHeight = 56.dp // Default height
    val collapsedHeight = 1.dp // Height when collapsed
    val progress = (scrollState.value / 100f).coerceIn(0f, 1f) // Change 200f to control the speed of collapsing

    val height = lerp(toolbarHeight, collapsedHeight, progress)

    Log.d("CollapsingTopAppBar","scroll: ${scrollState.value}")

    TopAppBar(
        modifier = modifier.height(height),
        backgroundColor = Color.LightGray,
        title = { title() }
    )
}