package com.example.kubik.presentation.announcement

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kubik.domain.announcement.model.AnnouncementItem
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.domain.announcement.model.AnnouncementType
import com.example.kubik.presentation.announcement.components.AnnouncementCard
import com.example.kubik.presentation.announcement.components.AnnouncementFilterTabs


@Composable
fun AnnouncementListScreen(
    innerPadding: PaddingValues,
    viewModel: AnnouncementViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AnnouncementListScreenContent(
        innerPadding = innerPadding,
        announcements = uiState.filteredAnnouncements,
        selectedFilter = uiState.selectedFilter,
        allCount = uiState.allCount,
        importantCount = uiState.importantCount,
        normalCount = uiState.normalCount,
        onFilterChange = viewModel::setFilter,
        onDeleteClick = viewModel::deleteAnnouncement,
        onBackClick = onBackClick
    )
}

@Composable
fun AnnouncementListScreenContent(
    innerPadding: PaddingValues,
    announcements: List<AnnouncementItem>,
    selectedFilter: AnnouncementFilter,
    allCount: Int,
    importantCount: Int,
    normalCount: Int,
    onFilterChange: (AnnouncementFilter) -> Unit,
    onDeleteClick: (String) -> Unit,
    onBackClick: () -> Unit
){
    val isDarkTheme = LocalIsDarkTheme.current
    val filters = listOf(
        AnnouncementFilter.ALL,
        AnnouncementFilter.IMPORTANT,
        AnnouncementFilter.NORMAL
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                top = innerPadding.calculateTopPadding() + 8.dp,
                start = 16.dp,
                end = 16.dp
            )
    ){
        Column{
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBackClick,
                    shape = CircleShape,
                    colors = iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .shadow(
                            elevation = 2.dp,
                            shape = CircleShape,
                            clip = false
                        )
                        .border(
                            1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                        .size(40.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Выйти на главную страницу",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(Modifier.weight(0.35f))
                Text(
                    "Объявления",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontFamily(
                        Font(R.font.inter_bold)
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(8.dp))
            AnnouncementFilterTabs(
                filters = filters,
                selectedFilter = selectedFilter,
                onFilterChange = onFilterChange,
                allCount = allCount,
                importantCount = importantCount,
                normalCount = normalCount,
                modifier = Modifier.fillMaxWidth()
            )
            if(announcements.isEmpty()){
                emptyCard(isDarkTheme)
            }
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(
                        items = announcements,
                        key = { it.id}
                    ){ state ->
                        AnnouncementCard(
                            announcement = state,
                            onDeleteClick = onDeleteClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun emptyCard(
    isDarkTheme: Boolean
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if(isDarkTheme) Color(0xFF0F172B) else Color(0xFFFFFFFF),
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.outline
            )
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .background(
                        color = if(isDarkTheme) Color(0xFF1D293D) else Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(16.dp)
                    ).padding(12.dp)
            ){
                Icon(
                    painter = painterResource(R.drawable.empty),
                    contentDescription = "Объявлений пока еще нет",
                    tint = Color(0xFF90A1B9)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Здесь пока пусто",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Таких объявлений еще не было",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 12.sp
            )
        }
    }
}

@PreviewLightDark
@Composable
fun previewListScreen() {
    KubikTheme {
        AnnouncementListScreenContent(
            innerPadding = PaddingValues(0.dp),
            announcements = listOf(
                AnnouncementItem(
                    id = "announcement_1",
                    groupId = "group_1",
                    title = "Перенос пар",
                    text = "Завтра первая пара отменяется, приходим ко второй (10:00). Не проспите!",
                    type = AnnouncementType.IMPORTANT.value,
                    authorId = "starosta_1",
                    authorName = "Иван Иванов",
                    createdAt = System.currentTimeMillis()
                ),
                AnnouncementItem(
                    id = "announcement_2",
                    groupId = "group_1",
                    title = "Материалы к практике",
                    text = "Файлы для подготовки загружены в раздел материалов группы.",
                    type = AnnouncementType.NORMAL.value,
                    authorId = "starosta_1",
                    authorName = "Иван Иванов",
                    createdAt = System.currentTimeMillis() - 86_400_000
                ),
                AnnouncementItem(
                    id = "announcement_3",
                    groupId = "group_1",
                    title = "Срочное объявление",
                    text = "До конца дня нужно подтвердить присутствие на завтрашней паре.",
                    type = AnnouncementType.IMPORTANT.value,
                    authorId = "starosta_1",
                    authorName = "Иван Иванов",
                    createdAt = System.currentTimeMillis() - 3_600_000
                )
            ),
            selectedFilter = AnnouncementFilter.ALL,
            allCount = 3,
            importantCount = 2,
            normalCount = 1,
            onFilterChange = {},
            onDeleteClick = {},
            {}
        )
    }
}