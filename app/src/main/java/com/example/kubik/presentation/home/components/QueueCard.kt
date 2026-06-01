package com.example.kubik.presentation.home.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun QueueCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    position: Int? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF8E51FF), Color(0xFF4F39F6))
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.16f),
                                shape = RoundedCornerShape(13.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Ближайшая очередь",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Перейти к ближайшей очереди",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
                Column() {
                    if(position != null && title != null) {
                        Text(
                            text = "Моя очередь",
                            fontFamily = FontFamily(
                                Font(R.font.inter_medium, FontWeight.Medium)
                            ),
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.78f)
                        )
                        Text(
                            text = "#$position",
                            fontFamily = FontFamily(
                                Font(R.font.inter_bold, FontWeight.Bold)
                            ),
                            fontSize = 36.sp,
                            color = Color.White
                        )
                        Text(
                            text = title,
                            fontFamily = FontFamily(
                                Font(R.font.inter_medium, FontWeight.Medium)
                            ),
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.88f)
                        )
                    }
                    else{
                        Text(
                            text = "Вы не записаны",
                            fontFamily = FontFamily(
                                Font(R.font.inter_medium, FontWeight.Medium)
                            ),
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.88f)
                        )
                        Text(
                            text = "Записаться в очередь",
                            fontFamily = FontFamily(
                                Font(R.font.inter_semibold, FontWeight.SemiBold)
                            ),
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }

            }
        }
    }
}

@PreviewLightDark
@Composable
fun previewQueueCard() {
    KubikTheme() {
        QueueCard(
            modifier = Modifier,
            title = null,
            position = 3,
            onClick = { }
        )
    }
}
