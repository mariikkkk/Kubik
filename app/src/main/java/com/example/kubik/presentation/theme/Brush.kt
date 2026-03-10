package com.example.kubik.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

object AppBrushes{
    val titleGradient: Brush
    @Composable
    get() = Brush.linearGradient(
        colors = listOf(
            titleGradient1,
            titleGradient2
        )
    )
}