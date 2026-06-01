package com.example.kubik.presentation.home.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.domain.announcement.model.AnnouncementItem
import com.example.kubik.domain.announcement.model.AnnouncementType
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.utils.toFormattedDate

@Composable
fun AnnouncementRow(
    announcement: AnnouncementItem,
    modifier: Modifier = Modifier
){
    val colorDot = getAnnouncementColor(announcement.type)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ){
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(10.dp)
                .background(
                    color = colorDot,
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = announcement.title,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_medium
                    )
                )
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = announcement.text,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            text = announcement.createdAt.toFormattedDate("dd.mm.yyyy, HH:mm"),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun getAnnouncementColor(
    status: String
): Color {
    return when(status){
        AnnouncementType.IMPORTANT.value -> {
            Color(0xFFFF2056)
        }
        AnnouncementType.NORMAL.value -> {
            Color(0xFF7C86FF)
        }

        else -> {
            Color(0xFF7C86FF)
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewAnnouncement(){
    KubikTheme {
        AnnouncementRow(
            announcement = AnnouncementItem(
                id = "announcement_1",
                groupId = "group_1",
                title = "Перенос пар",
                text = "Завтра первая пара отменяется, приходим ко второй (10:00). Не проспите!",
                type = AnnouncementType.NORMAL.value,
                authorId = "starosta_1",
                authorName = "Иван Иванов",
                createdAt = System.currentTimeMillis()
            )
        )
    }
}