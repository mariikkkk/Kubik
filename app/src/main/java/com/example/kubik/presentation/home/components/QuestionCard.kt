package com.example.kubik.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme

@Composable
fun QuestionCard(
    modifier: Modifier = Modifier,
    isStarosta: Boolean,
    questionCount: Int,
    onClick: () -> Unit
) {
    val iconId = if(isStarosta) R.drawable.questions else R.drawable.add
    val isDarkTheme = LocalIsDarkTheme.current
    val colorBackgroundIfStarosta = if(isDarkTheme) Color(0xFF35122D) else Color(0xFFFFE4E6)
    val colorContentIfStarosta = if(isDarkTheme) Color(0xFFFF637E) else Color(0xFFFF2056)
    val colorBackgroundIfStudent = if(isDarkTheme) Color(0xFF1A1D45) else Color(0xFFE0E7FF)
    val colorContentIfStudent = if(isDarkTheme) Color(0xFF7C86FF) else Color(0xFF615FFF)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                color = if(isStarosta) colorBackgroundIfStarosta else colorBackgroundIfStudent,
                                shape = RoundedCornerShape(13.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(iconId),
                            contentDescription = "Вопросы",
                            tint = if(isStarosta) colorContentIfStarosta else colorContentIfStudent,
                            modifier = Modifier.size(18.dp)
                        )

                    }
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Перейти к экрану вопросов",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Вопросы",
                    fontFamily = FontFamily(
                        Font(R.font.inter_medium, FontWeight.Medium)
                    ),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if(isStarosta){
                    Text(
                        text = "$questionCount",
                        fontFamily = FontFamily(
                            Font(R.font.inter_bold, FontWeight.Bold)
                        ),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    Text(
                        text = "Задать",
                        fontFamily = FontFamily(
                            Font(R.font.inter_bold, FontWeight.Bold)
                        ),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                if(isStarosta){
                    Text(
                        text = "Открытых",
                        fontFamily = FontFamily(
                            Font(R.font.inter_medium, FontWeight.Medium)
                        ),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "Староста ответит",
                        fontFamily = FontFamily(
                            Font(R.font.inter_medium, FontWeight.Medium)
                        ),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun previewQuestionCard1() {
    KubikTheme() {
        QuestionCard(
            isStarosta = true,
            questionCount = 3,
            onClick = { }
        )
    }
}

@PreviewLightDark
@Composable
fun previewQuestionCard2() {
    KubikTheme() {
        QuestionCard(
            isStarosta = false,
            questionCount = 3,
            onClick = { }
        )
    }
}
