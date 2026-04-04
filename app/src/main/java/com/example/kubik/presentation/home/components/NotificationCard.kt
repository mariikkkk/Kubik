package com.example.kubik.presentation.home.components

import android.app.Notification
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun NotificationCard(
    title: String,
    description: String,
    date: String
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(100.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.7f.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer.copy(0.3f))
                .height(IntrinsicSize.Min)
        ){
//            Box(
//                modifier = Modifier
//                    .background(MaterialTheme.colorScheme.primary)
//                    .fillMaxHeight()
//                    .width(8.dp),
//            )
            Box(
                modifier = Modifier
                    .heightIn(100.dp)
                    .background(MaterialTheme.colorScheme.background)
            ){
                Column(
                    modifier = Modifier.padding(16.dp)
                ){
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(title,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_semibold,
                                    FontWeight.SemiBold)
                            ),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(date,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_medium,
                                    FontWeight.Medium)
                            ),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(description,
                        fontFamily = FontFamily(
                            Font(
                                R.font.inter_regular,
                                FontWeight.Normal
                            )
                        ),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

    }

}

@PreviewLightDark
@Composable
fun previewNotCard() {
    KubikTheme() {
        NotificationCard(
            "Перенос пар ⚠\uFE0F",
            "Завтра первая пара отменяется, приходим ко второй (10:00). Не проспите!",
            "Сегодня, 14:30"
        )
    }
}