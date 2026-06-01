package com.example.kubik.presentation.announcement.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.kubik.domain.announcement.model.AnnouncementItem
import com.example.kubik.domain.announcement.model.AnnouncementType
import com.example.kubik.presentation.announcement.AnnouncementFilter
import com.example.kubik.presentation.announcement.AnnouncementUiState
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme
import com.example.kubik.presentation.utils.toFormattedDate

private data class AnnouncementStatusColors(
    val backgroundColor: Color,
    val lineColor: Color,
    val textColor: Color
)
@Composable
fun AnnouncementCard(
    announcement: AnnouncementItem,
    onDeleteClick: (String) -> Unit
){
    val statusColors = getAnnouncementStatusColors(announcement.type)
    val statusText = getAnnouncementStatusText(announcement.type)
    val statusIcon = getAnnouncementStatusIcon(announcement.type)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(100.dp)
            .combinedClickable(
                onLongClick = { onDeleteClick(announcement.id) },
                onClick = {}
            ),
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
            Box(
                modifier = Modifier
                    .background(statusColors.lineColor)
                    .fillMaxHeight()
                    .width(8.dp),
            )
            Box(
                modifier = Modifier
                    .heightIn(100.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ){
                Column(
                    modifier = Modifier.padding(16.dp)
                ){
                    Row( verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = statusColors.backgroundColor,
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ){
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Icon(
                                    painter = painterResource(statusIcon),
                                    contentDescription = "Тип объявления",
                                    tint = statusColors.textColor,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    statusText,
                                    fontFamily = FontFamily(
                                        Font(
                                            R.font.inter_bold,
                                            FontWeight.Bold
                                        )
                                    ),
                                    fontSize = 12.sp,
                                    color = statusColors.textColor
                                )
                            }
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(announcement.createdAt.toFormattedDate("dd.MM.yyyy, HH:mm"),
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_medium,
                                    FontWeight.Medium)
                            ),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(announcement.title,
                        fontFamily = FontFamily(
                            Font(
                                R.font.inter_semibold,
                                FontWeight.SemiBold)
                        ),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(announcement.text,
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

@Composable
private fun getAnnouncementStatusColors(
    status: String
): AnnouncementStatusColors {
    val isDarkTheme = LocalIsDarkTheme.current
    return when(status){
        AnnouncementType.IMPORTANT.value -> {
            if(isDarkTheme){
                AnnouncementStatusColors(
                    backgroundColor = Color(0xFF2E0A25),
                    lineColor = Color(0xFFFF442B),
                    textColor = Color(0xFFFF637E)
                )
            }else{
                AnnouncementStatusColors(
                    backgroundColor = Color(0xFFFFE4E6),
                    lineColor = Color(0xFFFF442B),
                    textColor = Color(0xFFEC003F)
                )
            }
        }
        AnnouncementType.NORMAL.value -> {
            if(isDarkTheme){
                AnnouncementStatusColors(
                    backgroundColor = Color(0xFF13153D),
                    lineColor = Color(0xFFAD46FF),
                    textColor = Color(0xFF7C86FF)
                )
            }else{
                AnnouncementStatusColors(
                    backgroundColor = Color(0xFFE0E7FF),
                    lineColor = Color(0xFFAD46FF),
                    textColor = Color(0xFF4F39F6)
                )
            }
        }

        else -> {AnnouncementStatusColors(
            backgroundColor = Color(0xFFE0E7FF),
            lineColor = Color(0xFFAD46FF),
            textColor = Color(0xFF4F39F6)
        )}
    }
}

private fun getAnnouncementStatusText(
    status: String): String {
    return when(status){
        AnnouncementType.IMPORTANT.value -> "Важное"
        AnnouncementType.NORMAL.value -> "Обычное"
        else -> "Обычное"
    }
}

private fun getAnnouncementStatusIcon(
    status: String
): Int{
    return when(status){
        AnnouncementType.IMPORTANT.value -> R.drawable.active
        AnnouncementType.NORMAL.value -> R.drawable.default_announcement
        else -> R.drawable.default_announcement
    }
}

@PreviewLightDark
@Composable
fun previewCard() {
    KubikTheme {
        AnnouncementCard(
            announcement = AnnouncementItem(
                id = "announcement_1",
                groupId = "group_1",
                title = "Перенос пар",
                text = "Завтра первая пара отменяется, приходим ко второй (10:00). Не проспите!",
                type = AnnouncementType.NORMAL.value,
                authorId = "starosta_1",
                authorName = "Иван Иванов",
                createdAt = System.currentTimeMillis()
            ),
            {}
        )
    }
}