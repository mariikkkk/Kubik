package com.example.kubik.presentation.queues

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kubik.R
import com.example.kubik.domain.queues.models.QueueItem
import com.example.kubik.domain.queues.models.QueueStatus
import com.example.kubik.presentation.components.FilterTabs
import com.example.kubik.presentation.components.SearchBar
import com.example.kubik.presentation.queues.components.QueueCreateDialog
import com.example.kubik.presentation.queues.components.QueueItemCard
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.glow

@Composable
fun QueuesListScreen(
    viewModel: QueuesListViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    onQueueClick: (String) -> Unit
){
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val filteredQueues by viewModel.filteredQueues.collectAsStateWithLifecycle()
    val showCreateDialog by viewModel.showCreateDialog.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    QueuesListScreenContent(
        innerPadding,
        searchQuery,
        {viewModel.updateSearchQuery(it)},
        { viewModel.setFilter(it) },
        selectedFilter,
        filteredQueues,
        { viewModel.showCreateDialog() },
        onQueueClick,
        isStarosta = currentUser?.role == "starosta"
    )

    if(showCreateDialog){
        QueueCreateDialog(
            { viewModel.hideCreateDialog() },
            { title, totalSlots, submissionDate ->
                viewModel.createQueue(title, totalSlots, submissionDate)
            }
        )
    }
}

@Composable
fun QueuesListScreenContent(
    innerPadding: PaddingValues,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onTitleChange: (QueueFilter) -> Unit,
    selectedFilter: QueueFilter,
    filteredQueues: List<QueueCardState>,
    onFabClick: () -> Unit,
    onQueueClick: (String) -> Unit,
    isStarosta: Boolean = false
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .padding(
                top = innerPadding.calculateTopPadding() + 8.dp,
                start = 16.dp,
                end = 16.dp
            )
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
        ){
//            Text(
//                "Очереди",
//                fontFamily = FontFamily(
//                    Font(
//                        R.font.inter_bold,
//                        FontWeight.Bold
//                    )
//                ),
//                color = MaterialTheme.colorScheme.onBackground,
//                fontSize = 24.sp
//            )
//            Spacer(modifier = Modifier.height(16.dp))
            SearchBar(
                searchQuery,
                onSearchQueryChange,
                "Поиск по названию...",
                Modifier
            )
            Spacer(modifier = Modifier.height(16.dp))
            FilterTabs(
                entries = QueueFilter.entries,
                selectedItem = selectedFilter,
                onItemClick = { onTitleChange(it) },
                itemTitle = { filter ->
                    when(filter) {
                        QueueFilter.ALL -> "Все"
                        QueueFilter.ACTIVE -> "Активные"
                        QueueFilter.MINE -> "Мои"
                        QueueFilter.CLOSED -> "Завершенные"
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(items = filteredQueues){ item ->
                    QueueItemCard(item,
                        { onQueueClick(item.queue.id) })
                }
            }
        }
        if(isStarosta){
            FloatingActionButton(
                onClick = onFabClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 0.dp)
                    .glow(
                        MaterialTheme.colorScheme.primary,
                        1f,
                        30.dp,
                        15.dp
                    ),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Добавить очередь",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun previewQueuesListScreen(){
    KubikTheme() {
        QueuesListScreenContent(
            innerPadding = PaddingValues(0.dp),
            searchQuery = "",
            {},
            {},
            QueueFilter.ACTIVE,
            listOf(
                QueueCardState(
                    queue = QueueItem(
                        id = "1",
                        groupId = "group1",
                        title = "Лабораторная по физике",
                        createdAt = 1715000000000L,
                        closedAt = null,
                        status = QueueStatus.ACTIVE.value,
                        totalSlots = 15,
                        currentActiveSlot = 3,
                        creatorId = "user1",
                        participantIds = listOf("u1", "u2", "u3", "u4", "u5"),
                        submissionDate = 1715200000000L
                    ),
                    slotNumber = 3
                ),
                QueueCardState(
                    queue = QueueItem(
                        id = "2",
                        groupId = "group1",
                        title = "Курсовая по ИБ",
                        createdAt = 1714900000000L,
                        closedAt = null,
                        status = QueueStatus.WAITING.value,
                        totalSlots = 8,
                        currentActiveSlot = null,
                        creatorId = "user1",
                        participantIds = listOf("u1", "u2"),
                        submissionDate = 1715400000000L
                    ),
                    slotNumber = null
                ),
                QueueCardState(
                    queue = QueueItem(
                        id = "3",
                        groupId = "group1",
                        title = "Защита проекта ТРПП",
                        createdAt = 1714800000000L,
                        closedAt = 1715100000000L,
                        status = QueueStatus.CLOSED.value,
                        totalSlots = 26,
                        currentActiveSlot = null,
                        creatorId = "user1",
                        participantIds = listOf("u1","u2","u3","u4","u5","u6","u7","u8"),
                        submissionDate = 1715000000000L
                    ),
                    slotNumber = null
                )
            ),
            {},
            {},
            isStarosta = true
        )
    }
}