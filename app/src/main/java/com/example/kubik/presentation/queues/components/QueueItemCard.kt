package com.example.kubik.presentation.queues.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.domain.queues.models.QueueItem
import com.example.kubik.domain.queues.models.QueueStatus
import com.example.kubik.presentation.queues.QueueCardState
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme
import com.example.kubik.presentation.utils.toFormattedDate

@Composable
fun QueueItemCard(
    state: QueueCardState,
    onQueueClick: () -> Unit
) {
    val isDarkTheme = LocalIsDarkTheme.current
    Card(
        onClick = onQueueClick,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    state.queue.title,
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_bold,
                            FontWeight.Bold
                        )
                    ),
                    fontSize = 18.sp
                )
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(
                            vertical = 6.dp,
                            horizontal = 12.dp
                        )
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painterResource(
                                R.drawable.group
                            ),
                            contentDescription = "Количество человек в очереди",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "${state.queue.participantIds.size}/${state.queue.totalSlots}",
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_semibold,
                                    FontWeight.SemiBold
                                )
                            ),
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                state.queue.submissionDate?.toFormattedDate() ?: "",
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_regular,
                        FontWeight.Normal
                    )
                ),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Создана: ${state.queue.createdAt.toFormattedDate("d MMMM 'в' HH:mm")}",
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_regular,
                        FontWeight.Normal
                    )
                ),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onTertiary
            )
            if(state.queue.closedAt != null){
                Text(
                    "Закрыта: ${state.queue.closedAt.toFormattedDate("d MMMM 'в' HH:mm")}",
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_regular,
                            FontWeight.Normal
                        )
                    ),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.offset(y=(-4).dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = if(state.queue.currentActiveSlot != null){
                            if(isDarkTheme) Color(0xFF7B3306).copy(0.3f)
                            else Color(0xFFFEF3C6)

                        } else{
                            if(isDarkTheme) Color(0xFF314158) else Color(0xFFE2E8F0)
                        },
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(vertical = 4.dp, horizontal = 10.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(state.queue.currentActiveSlot != null && state.queue.status == QueueStatus.ACTIVE.value){
                        Icon(
                            painterResource(
                                R.drawable.attention
                            ),
                            contentDescription = "Номер текущего сдающего",
                            tint = if(isDarkTheme) Color(0xFFFFB900) else Color(0xFFE17100),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "Сдает: №${state.queue.currentActiveSlot}",
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_semibold,
                                    FontWeight.SemiBold
                                )
                            ),
                            fontSize = 12.sp,
                            color = if(isDarkTheme) Color(0xFFFFB900) else Color(0xFFE17100)
                        )
                    } else if(state.queue.status == QueueStatus.CLOSED.value){
                        Text(
                            "Завершена",
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_semibold,
                                    FontWeight.SemiBold
                                )
                            ),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else{
                        Text(
                            "Ожидает запуска",
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_semibold,
                                    FontWeight.SemiBold
                                )
                            ),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            if(state.slotNumber != null){
                Text(
                    "Ваше место: №${state.slotNumber}",
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_bold,
                            FontWeight.Bold
                        )
                    ),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
            progress = {
                if(state.queue.totalSlots == 0) 0f
                            else state.queue.participantIds.size.toFloat() / state.queue.totalSlots.toFloat()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(20.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.outline,
            strokeCap = StrokeCap.Round,
                gapSize = 0.dp,
                drawStopIndicator = {}
            )
        }
    }
}

@PreviewLightDark
@Composable
fun previewQueueItemCard(){
    KubikTheme {
        QueueItemCard(
            QueueCardState(
                QueueItem(
                    "1",
                    "1231",
                    "Лаба по физике",
                    1321485348L,
                    343853495L,
                    QueueStatus.ACTIVE.value,
                    10,
                    3,
                    "123",
                    listOf("213","3123"),
                    324324234634L
                ),
                2
            ),
            {}
        )
    }
}
