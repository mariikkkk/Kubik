package com.example.kubik.presentation.queues.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.kubik.domain.queues.models.QueueStatus
import com.example.kubik.domain.queues.models.SlotItem
import com.example.kubik.domain.queues.models.SlotStatus
import com.example.kubik.presentation.queues.SlotDisplayItem
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme

@Composable
fun SlotCard(
    item: SlotDisplayItem,
    onSlotClick: (Int) -> Unit,
    onLeaveClick: (Int) -> Unit,
    queueStatus: QueueStatus,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = LocalIsDarkTheme.current
    val colors = item.resolveColors(isDarkTheme)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(72.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.backgroundColor,
        ),
        border = BorderStroke(
            1.dp,
            colors.borderColor
        ),
        enabled = if(queueStatus == QueueStatus.CLOSED) false else true,
        onClick = {
            when{
                item.slot == null && !item.isBlocked -> onSlotClick(item.slotNumber)
                item.isCurrentUser && item.slot?.typedStatus == SlotStatus.WAITING -> onLeaveClick(item.slotNumber)
            }
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(72.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        shape = CircleShape,
                        color = colors.circleBackgroundColor
                    ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = item.slotNumber.toString(),
                    color = colors.circleTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                when {
                    item.isBlocked ->
                        Text(
                            "Заблокировано",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    item.slot == null ->
                        Text(
                            "Свободно",
                            fontSize = 16.sp,
                            color = colors.textColor
                        )
                    else ->
                    {
                        Text(
                            text = item.slot.userName,
                            color = colors.textColor,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val (icon, iconColor, statusText) = when{
                                item.isActiveSlot -> Triple(
                                    R.drawable.active,
                                    Color(0xFFFE9A00),
                                    "Сдает"
                                )
                                item.slot.typedStatus == SlotStatus.PASSED -> Triple(
                                    R.drawable.passed,
                                    Color(0xFF00BC7D),
                                    "Сдал"
                                )
                                item.slot.typedStatus == SlotStatus.FAILED -> Triple(
                                    R.drawable.failed,
                                    Color(0xFFFF637E),
                                    "Не сдал"
                                )
                                else -> Triple(
                                    R.drawable.waiting,
                                    Color(0xFF90A1B9),
                                    "Ожидает"
                                )
                            }
                            Icon(
                                painterResource(icon),
                                contentDescription = "Статус слота",
                                tint = iconColor
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                statusText,
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.inter_regular
                                    )
                                ),
                                fontSize = 12.sp,
                                color = iconColor
                            )

                        }
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            when{
                item.isBlocked -> {
                    Icon(
                        painterResource(R.drawable.lock),
                        contentDescription = "Заблокировано",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                item.slot == null -> {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFF615FFF).copy(0.1f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                    ){ Text(
                            "Занять",
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_medium,
                                    FontWeight.Medium
                                )
                            ),
                            fontSize = 14.sp,
                            color = Color(0xFF615FFF)
                        )
                    }
                }
                item.isCurrentUser -> {
                    Icon(
                        painter = painterResource(R.drawable.people),
                        contentDescription = "Мой слот",
                        tint = Color(0xFFF1F5F9),
                        modifier = Modifier.size(18.dp)
                        )
                }
                item.slot.typedStatus == SlotStatus.FAILED -> {
                    Icon(
                        painterResource(R.drawable.failed),
                        contentDescription = "Не сдал",
                        tint = Color.Unspecified
                    )
                }
                item.slot.typedStatus == SlotStatus.PASSED -> {
                    Icon(
                        painterResource(R.drawable.passed),
                        contentDescription = "Сдал",
                        tint = Color.Unspecified
                    )
                }

            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewSlotCard(){
    KubikTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Активный - сдаёт
            SlotCard(
                item = SlotDisplayItem(
                    slotNumber = 1,
                    slot = SlotItem(1, "user1", "Алексей С.", SlotStatus.WAITING.value),
                    isBlocked = false,
                    isCurrentUser = false,
                    isActiveSlot = true
                ),
                onSlotClick = {},
                onLeaveClick = {},
                queueStatus = QueueStatus.ACTIVE
            )
            // Ожидает
            SlotCard(
                item = SlotDisplayItem(
                    slotNumber = 2,
                    slot = SlotItem(2, "user2", "Мария В.", SlotStatus.WAITING.value),
                    isBlocked = false,
                    isCurrentUser = false,
                    isActiveSlot = false
                ),
                onSlotClick = {},
                onLeaveClick = {},
                queueStatus = QueueStatus.ACTIVE
            )
            // Я - жду
            SlotCard(
                item = SlotDisplayItem(
                    slotNumber = 3,
                    slot = SlotItem(3, "me", "Марат", SlotStatus.WAITING.value),
                    isBlocked = false,
                    isCurrentUser = true,
                    isActiveSlot = false
                ),
                onSlotClick = {},
                onLeaveClick = {},
                queueStatus = QueueStatus.ACTIVE
            )
            // Сдал
            SlotCard(
                item = SlotDisplayItem(
                    slotNumber = 4,
                    slot = SlotItem(4, "user3", "Кирилл Д.", SlotStatus.PASSED.value),
                    isBlocked = false,
                    isCurrentUser = false,
                    isActiveSlot = false
                ),
                onSlotClick = {},
                onLeaveClick = {},
                queueStatus = QueueStatus.ACTIVE
            )
            // Не сдал
            SlotCard(
                item = SlotDisplayItem(
                    slotNumber = 5,
                    slot = SlotItem(5, "user4", "Дарья К.", SlotStatus.FAILED.value),
                    isBlocked = false,
                    isCurrentUser = false,
                    isActiveSlot = false
                ),
                onSlotClick = {},
                onLeaveClick = {},
                queueStatus = QueueStatus.ACTIVE
            )
            // Свободно
            SlotCard(
                item = SlotDisplayItem(
                    slotNumber = 6,
                    slot = null,
                    isBlocked = false,
                    isCurrentUser = false,
                    isActiveSlot = false
                ),
                onSlotClick = {},
                onLeaveClick = {},
                queueStatus = QueueStatus.CLOSED
            )
            // Заблокировано
            SlotCard(
                item = SlotDisplayItem(
                    slotNumber = 7,
                    slot = null,
                    isBlocked = true,
                    isCurrentUser = false,
                    isActiveSlot = false
                ),
                onSlotClick = {},
                onLeaveClick = {},
                queueStatus = QueueStatus.ACTIVE
            )
        }
    }
}