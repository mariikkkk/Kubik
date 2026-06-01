package com.example.kubik.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.domain.announcement.model.AnnouncementItem
import com.example.kubik.domain.announcement.model.AnnouncementType
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun HomeAnnouncementCard(
    announcements: List<AnnouncementItem>,
    onShowAllClick: () -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline
        )
    ) {
        Spacer(Modifier.height(8.dp))
        announcements.take(3).forEachIndexed { index, item ->
            AnnouncementRow(
                announcement = item
            )
            if(index != announcements.take(3).lastIndex){
                HorizontalDivider()
            }
        }
        HorizontalDivider()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clickable{ onShowAllClick() },
            contentAlignment = Alignment.Center

        ){
            Text(
                text = "Все объявления >",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewHomeAnnouncementCard(){
    KubikTheme {
        HomeAnnouncementCard(
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
            {}
        )
    }
}