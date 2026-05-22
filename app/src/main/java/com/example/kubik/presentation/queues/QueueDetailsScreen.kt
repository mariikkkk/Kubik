package com.example.kubik.presentation.queues

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kubik.R
import com.example.kubik.domain.queues.models.QueueItem
import com.example.kubik.domain.queues.models.QueueStatus
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.domain.queues.models.SlotItem
import com.example.kubik.domain.queues.models.SlotStatus
import com.example.kubik.presentation.queues.components.CustomRewriteAttentionDialog
import com.example.kubik.presentation.queues.components.QueueCloseDialog
import com.example.kubik.presentation.queues.components.QueueDeleteDialog
import com.example.kubik.presentation.queues.components.QueueDetailsInfoCard
import com.example.kubik.presentation.queues.components.QueueEditDialog
import com.example.kubik.presentation.queues.components.QueueSlotsList
import com.example.kubik.presentation.theme.LocalIsDarkTheme

@Composable
fun QueueDetailsScreen(
    viewModel: QueueDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    innerPadding: PaddingValues
) {
    val queue by viewModel.currentQueue.collectAsStateWithLifecycle()
    val slots by viewModel.slotList.collectAsStateWithLifecycle()
    val isStarosta by viewModel.isStarosta.collectAsStateWithLifecycle()
    val myWaitingSlot by viewModel.myWaitingSlot.collectAsStateWithLifecycle()
    val showEditSheet by viewModel.showEditDialog.collectAsStateWithLifecycle()
    val showCloseDialog by viewModel.showCloseDialog.collectAsStateWithLifecycle()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsStateWithLifecycle()
    val rebookDialog by viewModel.rebookDialog.collectAsStateWithLifecycle()
    QueueDetailsScreenContent(
        queue = queue,
        slots = slots,
        isStarosta = isStarosta,
        myWaitingSlot = myWaitingSlot,
        onBackClick = onBackClick,
        innerPadding = innerPadding,
        onSlotClick = viewModel::onSlotClick,
        onLeaveClick = viewModel::leaveQueue,
        onEditClick = { viewModel.openEditDialog() },
        onDeleteClick = { viewModel.openDeleteDialog() },
        onPassedClick = viewModel::markCurrentSlotPassed,
        onFailedClick = viewModel::markCurrentSlotFailed,
        onStartClick = viewModel::startQueue,
        onCloseClick = { viewModel.openCloseDialog() }

    )

    if(rebookDialog != null){
        CustomRewriteAttentionDialog(
            oldPosition = rebookDialog!!.fromSlot,
            newPosition = rebookDialog!!.toSlot,
            onDismiss = viewModel::dismissRebook,
            onConfirm = {
                viewModel.confirmRebook(rebookDialog!!.fromSlot, rebookDialog!!.toSlot)
            }
        )

    }

    if (showEditSheet) {
        queue?.let { currentQueue ->
            QueueEditDialog(
                initialTitle = currentQueue.title,
                initialTotalSlots = currentQueue.totalSlots,
                onDismiss = viewModel::hideEditDialog,
                onEdit = { title, totalSlots ->
                    viewModel.editQueue(title, totalSlots)
                    viewModel.hideEditDialog()
                }
            )
        }
    }
    if(showDeleteDialog){
        QueueDeleteDialog(
            onDismiss = viewModel::hideDeleteDialog,
            onConfirm = {
                viewModel.hideDeleteDialog()
                viewModel.deleteQueue()
                onBackClick()
            }
        )
    }
    if(showCloseDialog){
        QueueCloseDialog(
            onDismiss = viewModel::hideCloseDialog,
            onMigrate = viewModel::closeQueueWithMigration,
            onClose = viewModel::closeQueueWithoutMigration,
            totalSlots = queue?.totalSlots ?: 0,
            waitingStudents = slots.count { it.slot?.typedStatus == SlotStatus.WAITING }
        )
    }
}

@Composable
fun QueueDetailsScreenContent(
    queue: QueueItem?,
    slots: List<SlotDisplayItem>,
    isStarosta: Boolean,
    myWaitingSlot: SlotDisplayItem?,
    onBackClick: () -> Unit,
    innerPadding: PaddingValues,
    onSlotClick: (Int) -> Unit,
    onLeaveClick: (Int) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onPassedClick: () -> Unit,
    onFailedClick: () -> Unit,
    onStartClick: () -> Unit,
    onCloseClick: () -> Unit
){
    val isDarkTheme = LocalIsDarkTheme.current
    val editColorContainer = if(isDarkTheme) Color(0xFF101139) else Color(0xFFE0E7FF)
    val editColorContent = if(isDarkTheme) Color(0xFF7C86FF) else Color(0xFF4f39f6)
    val deleteColorContainer = if(isDarkTheme) Color(0xFF2B0621) else Color(0xFFFFE4E6)
    val deleteColorContent = if(isDarkTheme) Color(0xFFFF637E) else Color(0xFFEC003F)
    Box(
        Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(
                top = innerPadding.calculateTopPadding() + 8.dp,
                start = 16.dp,
                end = 16.dp
            )
        ){
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                QueueIconActionButton(
                    onClick = onBackClick,
                    icon = Icons.Default.Close,
                    contentDescription = "Вернуться к списку очередей",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = queue?.title ?: "",
                    modifier = Modifier.weight(1f),
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_bold,
                            FontWeight.Bold
                        )
                    ),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if(isStarosta){
                    QueueIconActionButton(
                        onClick = onEditClick,
                        iconRes = R.drawable.edit_queue,
                        contentDescription = "Редактировать очередь",
                        containerColor = editColorContainer,
                        contentColor = editColorContent
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    QueueIconActionButton(
                        onClick = onDeleteClick,
                        iconRes = R.drawable.delete_queue,
                        contentDescription = "Удалить очередь",
                        containerColor = deleteColorContainer,
                        contentColor = deleteColorContent
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            if (queue != null) {
                QueueDetailsInfoCard(
                    queue = queue,
                    isStarosta = isStarosta,
                    myWaitingSlot = myWaitingSlot,
                    onPassedClick = onPassedClick,
                    onFailedClick = onFailedClick,
                    onLeaveClick = {
                        myWaitingSlot?.let { onLeaveClick(it.slotNumber) }
                    },
                    onStartClick = onStartClick,
                    onCloseClick = onCloseClick
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Список очереди",
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_semibold,
                        FontWeight.SemiBold
                    )
                ),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(16.dp))
            if(queue != null){
                QueueSlotsList(
                    slots = slots,
                    onSlotClick = onSlotClick,
                    onLeaveClick = onLeaveClick,
                    queueStatus = queue.typedStatus
                )
            }
        }
    }
}

@Composable
private fun QueueIconActionButton(
    onClick: () -> Unit,
    icon: ImageVector? = null,
    iconRes: Int? = null,
    contentDescription: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
){
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(38.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = CircleShape
    ) {
        if(icon != null){
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(20.dp)
            )
        } else{
            Icon(
                painter = painterResource(id = iconRes!!),
                contentDescription = contentDescription,
                modifier = Modifier.size(20.dp)
            )
        }

    }
}

@PreviewLightDark
@Composable
fun previewQueueDetailsScreen(){
    KubikTheme() {
        QueueDetailsScreenContent(
            queue = QueueItem(
                id = "1",
                groupId = "group1",
                title = "Защита лабораторной",
                createdAt = 34173483247L,
                closedAt = null,
                status = "active",
                totalSlots = 5,
                currentActiveSlot = 2,
                creatorId = "user1",
                submissionDate = 2323475435L
            ),
            slots = listOf(
                SlotDisplayItem(1, null, isBlocked = false, isCurrentUser = false, isActiveSlot = false),
                SlotDisplayItem(2, SlotItem(2, "user2", "Иван", "waiting"), isBlocked = false, isCurrentUser = false, isActiveSlot = true),
                SlotDisplayItem(3, SlotItem(3, "user3", "Петр", "waiting"), isBlocked = false, isCurrentUser = true, isActiveSlot = false),
                SlotDisplayItem(4, null, isBlocked = true, isCurrentUser = false, isActiveSlot = false),
                SlotDisplayItem(5, null, isBlocked = false, isCurrentUser = false, isActiveSlot = false)
            ),
            isStarosta = true,
            myWaitingSlot = SlotDisplayItem(2, SlotItem(2, "user2", "Иван", "waiting"), isBlocked = false, isCurrentUser = true, isActiveSlot = true),
            onBackClick = {},
            innerPadding = PaddingValues(),
            onSlotClick = {},
            onLeaveClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onPassedClick = {},
            onFailedClick = {},
            onStartClick = {},
            onCloseClick = {}
        )
    }
}