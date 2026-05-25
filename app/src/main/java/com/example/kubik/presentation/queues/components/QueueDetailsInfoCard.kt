package com.example.kubik.presentation.queues.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.domain.queues.models.QueueItem
import com.example.kubik.domain.queues.models.QueueStatus
import com.example.kubik.domain.queues.models.SlotItem
import com.example.kubik.domain.queues.models.SlotStatus
import com.example.kubik.presentation.queues.SlotDisplayItem
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme
import com.example.kubik.presentation.utils.toFormattedDate

@Composable
fun QueueDetailsInfoCard(
    queue: QueueItem,
    isStarosta: Boolean,
    myWaitingSlot: SlotDisplayItem?,
    onPassedClick: () -> Unit,
    onFailedClick: () -> Unit,
    onLeaveClick: () -> Unit,
    onStartClick: () -> Unit,
    onCloseClick: () -> Unit
){
    val canStart = isStarosta && queue.typedStatus == QueueStatus.WAITING
    val canClose = isStarosta && queue.typedStatus == QueueStatus.ACTIVE
    val canLeave = myWaitingSlot != null
            && myWaitingSlot.slot?.typedStatus == SlotStatus.WAITING
    val canMarkResult = myWaitingSlot?.slotNumber == queue.currentActiveSlot
            && myWaitingSlot?.slot?.typedStatus == SlotStatus.WAITING
            && queue.typedStatus == QueueStatus.ACTIVE
    val isDarkTheme = LocalIsDarkTheme.current
    val passedColorContent = if(isDarkTheme) Color(0xFF00D492) else Color(0xFF007A55)
    val passedColorContainer = if(isDarkTheme) Color(0xFF0B2830) else Color(0xFFD0FAE5)
    val failedColorContainer = if(isDarkTheme) Color(0xFF301F20) else Color(0xFFFEF3C6)
    val failedColorContent = if(isDarkTheme) Color(0xFFFFB900) else Color(0xFFBB4D00)
    val leaveColorContainer = if(isDarkTheme) Color(0xFF35122E) else Color(0xFFFFE4E6)
    val leaveColorContent = if(isDarkTheme) Color(0xFFFF637E) else Color(0xFFEC003F)
    val startColorContainer = Color(0xFF009966)
    val startColorContent = Color(0xFFFFFFFF)
    val closeColorContainer = Color(0xFF4F39F6)
    val closeColorContent = Color(0xFFFFFFFF)
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(21.dp)
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    queue.submissionDate?.toFormattedDate() ?: "",
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_medium,
                            FontWeight.Medium
                        )
                    ),
                    fontSize = 18.sp
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "Создана: ${queue.createdAt.toFormattedDate("d MMMM 'в' HH:mm")}",
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_regular,
                            FontWeight.Normal
                        )
                    ),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (queue.typedStatus == QueueStatus.ACTIVE && queue.currentActiveSlot != null) {
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .background(
                            color =
                                if (isDarkTheme) Color(0xFF7B3306).copy(0.3f)
                                else Color(0xFFFEF3C6),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(vertical = 4.dp, horizontal = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painterResource(
                                R.drawable.attention
                            ),
                            contentDescription = "Номер текущего сдающего",
                            tint = if (isDarkTheme) Color(0xFFFFB900) else Color(0xFFE17100),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "Сдает: №${queue.currentActiveSlot}",
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_semibold,
                                    FontWeight.SemiBold
                                )
                            ),
                            fontSize = 12.sp,
                            color = if (isDarkTheme) Color(0xFFFFB900) else Color(0xFFE17100)
                        )
                    }
                }
            }
            if(canMarkResult){
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QueueActionButton(
                        "Я сдал",
                        onPassedClick,
                        R.drawable.passed,
                        passedColorContent,
                        passedColorContainer,
                        Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    QueueActionButton(
                        "Я не сдал",
                        onFailedClick,
                        R.drawable.failed,
                        failedColorContent,
                        failedColorContainer,
                        Modifier.weight(1f)
                    )
                }
            }
            if (canLeave){
                Spacer(Modifier.height(16.dp))
                QueueActionButton(
                    "Покинуть очередь",
                    onLeaveClick,
                    contentColor = leaveColorContent,
                    containerColor = leaveColorContainer
                )
            }
            if(canStart){
                Spacer(Modifier.height(16.dp))
                QueueActionButton(
                    "Запустить очередь",
                    onStartClick,
                    R.drawable.start_queue,
                    startColorContent,
                    startColorContainer
                )
            }
            if(canClose){
                Spacer(Modifier.height(16.dp))
                QueueActionButton(
                    "Сдача окончена",
                    onCloseClick,
                    R.drawable.close_queue,
                    closeColorContent,
                    closeColorContainer,
                )
            }
        }
    }
}

@Composable
    private fun QueueActionButton(
    text: String,
    onClick: () -> Unit,
    iconRes: Int? = null,
    contentColor: Color,
    containerColor: Color,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        if(iconRes != null){
            Icon(
                painter = painterResource(
                    iconRes
                ),
                contentDescription = text
            )
            Spacer(Modifier.width(8.dp))
        }

        Text(
            text,
            fontFamily = FontFamily(
                Font(
                    R.font.inter_semibold,
                    FontWeight.SemiBold
                )
            ),
            fontSize = 16.sp,
            maxLines = 1
        )
    }
}

@PreviewLightDark
@Composable
fun PreviewQueueDetailsInfoCard(){
    KubikTheme() {
        QueueDetailsInfoCard(
            QueueItem(
            id = "1",
            groupId = "group1",
            title = "Лабораторная по физике",
            createdAt = 1715000000000L,
            closedAt = null,
            status = QueueStatus.ACTIVE.value,
            totalSlots = 15,
            currentActiveSlot = 1,
            creatorId = "user1",
            participantIds = listOf("u1", "u2", "u3", "u4", "u5"),
            submissionDate = 1715200000000L
        ),
            isStarosta = true,
            myWaitingSlot = SlotDisplayItem(
                slotNumber = 1,
                slot = SlotItem(
                    slotNumber = 1,
                    userId = "user1",
                    userName = "Алексей С.",
                    status = SlotStatus.WAITING.value
                ),
                isBlocked = false,
                isCurrentUser = false,
                isActiveSlot = true
            ),
            {},
            {},
            {},
            {},
            {})
    }
}