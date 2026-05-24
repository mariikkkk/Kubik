package com.example.kubik.presentation.home.components

import android.media.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme

@Composable
fun QueueCard(
    modifier: Modifier = Modifier,
    title: String,
    position: Int,
    onClick: () -> Unit
){
    val isDarkTheme = LocalIsDarkTheme.current
    val mainColor = Color(0xFF00BC7D)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable{ onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = RoundedCornerShape(18.dp)
//        border = BorderStroke(1.2f.dp, MaterialTheme.colorScheme.outline),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Column(
                    modifier = Modifier
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painter = painterResource(R.drawable.queue),
                            contentDescription = "Ближайшая очередь",
                            tint = mainColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = "Очередь: $title",
                            fontFamily = FontFamily(
                                Font(R.font.inter_medium, FontWeight.Medium)
                            ),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Row(){
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.inter_medium,
                                                FontWeight.Medium
                                            )
                                        ),
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 20.sp
                                    )
                                ) {
                                    append("Твоя позиция: ")
                                }
                                withStyle(
                                    SpanStyle(
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.inter_medium,
                                                FontWeight.Medium
                                            )
                                        ),
                                        color = mainColor,
                                        fontSize = 20.sp
                                    )
                                ){
                                    append("#${position.toString()}")
                                }
                            }
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "Перейти к ближайшей очереди",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun previewQueueCard(){
    KubikTheme() {
        QueueCard(
            modifier = Modifier,
            title = "Матан",
            position = 3,
            onClick = {  }
        )
    }
}
