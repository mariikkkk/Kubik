package com.example.kubik.presentation.login.component


import android.graphics.BlurMaskFilter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.example.kubik.presentation.theme.KubikTheme

val BackgroundColor = Color(0xFF020618)
val SpotColor1 = Color(0xFFAD46FF)
val SpotColor2 = Color(0xFF615FFF)
val SpotColor3 = Color(0xFF9810FA)
val SpotColor4 = Color(0xFF4F39F6)

@Composable
fun LoginBackground(content: @Composable BoxScope.() -> Unit){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind{
                drawRect(color = BackgroundColor, size = size)

                drawIntoCanvas { canvas ->
                    val paint = Paint().asFrameworkPaint()                  // Кисточка
                    paint.apply {                                           // Открытие настройки кисти
                        color = SpotColor4.copy(alpha = 0.15f).toArgb()     // Цвет кисти (старый Android понимает только ARGB)
                        maskFilter = BlurMaskFilter(1000f, BlurMaskFilter.Blur.NORMAL)  // Накладывание фильтра размытия
                    }

                    canvas.nativeCanvas.drawCircle(
                        size.width * 0.2f,              // Координата центра по иксу
                        size.height * 0.2f,             // Координата центра по игрику
                        size.width / 1.5f, // Радиус пятна
                        paint
                    )
                }

                drawIntoCanvas { canvas ->
                    val paint = Paint().asFrameworkPaint()
                    paint.apply {
                        color = SpotColor1.copy(alpha = 0.1f).toArgb()
                        maskFilter = BlurMaskFilter(1000f, BlurMaskFilter.Blur.NORMAL)
                    }

                    canvas.nativeCanvas.drawCircle(
                        size.width * 0.9f,
                        size.height * 0.4f,
                        size.width / 5f, // Радиус пятна
                        paint
                    )
                }

                drawIntoCanvas { canvas ->
                    val paint = Paint().asFrameworkPaint()
                    paint.apply {
                        color = SpotColor3.copy(alpha = 0.12f).toArgb()
                        maskFilter = BlurMaskFilter(1000f, BlurMaskFilter.Blur.NORMAL)
                    }

                    canvas.nativeCanvas.drawCircle(
                        size.width * 0.8f,
                        size.height * 0.7f,
                        size.width / 1.5f, // Радиус пятна
                        paint
                    )
                }

                drawIntoCanvas { canvas ->
                    val paint = Paint().asFrameworkPaint()
                    paint.apply {
                        color = SpotColor2.copy(alpha = 0.15f).toArgb()
                        maskFilter = BlurMaskFilter(1000f, BlurMaskFilter.Blur.NORMAL)
                    }

                    canvas.nativeCanvas.drawCircle(
                        size.width * 0.15f,
                        size.height * 0.9f,
                        size.width / 5f, // Радиус пятна
                        paint
                    )
                }
            }
    ){
        content()
    }
}

@Preview
@Composable
fun previewLoginBack(){
    KubikTheme() {
        LoginBackground {  }
    }
}