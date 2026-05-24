package com.example.kubik.presentation.queues.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.kubik.domain.queues.models.QueueStatus
import com.example.kubik.domain.queues.models.SlotItem
import com.example.kubik.presentation.queues.SlotDisplayItem
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun QueueSlotsList(
    slots: List<SlotDisplayItem>,
    onSlotClick: (Int) -> Unit,
    onLeaveClick: (Int) -> Unit,
    queueStatus: QueueStatus
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        slots.forEach { item ->
            SlotCard(
                item = item,
                onSlotClick = onSlotClick,
                onLeaveClick = onLeaveClick,
                queueStatus = queueStatus
            )
        }
    }
}

@PreviewLightDark
@Composable
fun previewSlotsList(){
    KubikTheme() {
        QueueSlotsList(
            slots = listOf(
                SlotDisplayItem(
                    slotNumber = 1,
                    slot = null,
                    isBlocked = false,
                    isCurrentUser = false,
                    isActiveSlot = false
                ),
                SlotDisplayItem(
                    slotNumber = 2,
                    slot = null,
                    isBlocked = true,
                    isCurrentUser = false,
                    isActiveSlot = false
                ),
                SlotDisplayItem(
                    slotNumber = 3,
                    slot = null,
                    isBlocked = false,
                    isCurrentUser = true,
                    isActiveSlot = false
                ),
                SlotDisplayItem(
                    slotNumber = 4,
                    slot = SlotItem(
                        slotNumber = 4,
                        userId = "user3",
                        userName = "Кирилл Д.",
                        status = "active"
                    ),
                    isBlocked = false,
                    isCurrentUser = false,
                    isActiveSlot = true
                )
            ),
            onSlotClick = {},
            onLeaveClick = {},
            queueStatus = QueueStatus.CLOSED
        )
    }
}