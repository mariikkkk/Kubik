package com.example.kubik.presentation.theme

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.glow(
    color: Color,
    alpha: Float = 0.2f,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 24.dp
) = this.drawBehind{
    val glowColor = color.copy(alpha = alpha).toArgb()

    this.drawIntoCanvas { canvas ->
        val paint = Paint().apply{
            val frameworkPaint = this.asFrameworkPaint()
            frameworkPaint.color = glowColor
            frameworkPaint.maskFilter = BlurMaskFilter(
                blurRadius.toPx(),
                BlurMaskFilter.Blur.NORMAL
            )
        }

        canvas.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            borderRadius.toPx(),
            borderRadius.toPx(),
            paint
        )
    }
}