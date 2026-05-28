package com.example.kubik.presentation.announcement.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.presentation.announcement.AnnouncementFilter
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun AnnouncementFilterTabs(
    filters: List<AnnouncementFilter>,
    selectedFilter: AnnouncementFilter,
    onFilterChange: (AnnouncementFilter) -> Unit,
    allCount: Int,
    importantCount: Int,
    normalCount: Int,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        filters.forEach { filter ->
        val isSelected = selectedFilter == filter
            AnnouncementFilterChip(
                title = getAnnouncementFilterTitle(filter),
                count = getAnnouncementFilterCount(
                    filter,
                    allCount,
                    normalCount,
                    importantCount),
                isSelected = isSelected,
                announcementFilterColors = getAnnouncementFilterColors(filter, isSelected),
                { onFilterChange(filter) }
            )
        }
    }
}

@Composable
private fun AnnouncementFilterChip(
    title: String,
    count: Int,
    isSelected: Boolean,
    announcementFilterColors: AnnouncementFilterColors,
    onClick: () -> Unit
){
    Box(
        modifier = Modifier
            .background(
                color = announcementFilterColors.backgroundColor,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = if(isSelected) (-1).dp else 1.dp,
                color = announcementFilterColors.borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable( onClick = onClick )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = announcementFilterColors.dotColor,
                        shape = CircleShape
                    )
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = title,
                color = announcementFilterColors.titleColor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Medium
            )
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = announcementFilterColors.countBackgroundColor,
                        shape = CircleShape
                    )
                    .width(24.dp)
                    .height(18.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    count.toString(),
                    color = announcementFilterColors.countTextColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        platformStyle =  PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }
        }
    }
}

private fun getAnnouncementFilterCount(
    filter: AnnouncementFilter,
    allCount: Int,
    normalCount: Int,
    importantCount: Int
): Int{
    return when(filter){
        AnnouncementFilter.NORMAL -> normalCount
        AnnouncementFilter.ALL -> allCount
        AnnouncementFilter.IMPORTANT -> importantCount
    }
}

private fun getAnnouncementFilterTitle(filter: AnnouncementFilter): String{
    return when(filter){
        AnnouncementFilter.IMPORTANT -> "Важное"
        AnnouncementFilter.ALL -> "Все"
        AnnouncementFilter.NORMAL -> "Обычное"
    }
}

@Composable
private fun getAnnouncementFilterColors(
    filter: AnnouncementFilter,
    isSelected: Boolean
) : AnnouncementFilterColors{
    val allColor = Color(0xFF90A1B9)
    val importantColor = Color(0xFFFF2056)
    val normalColor = Color(0xFF615FFF)
    val dotColor = when(filter){
        AnnouncementFilter.NORMAL -> normalColor
        AnnouncementFilter.ALL -> allColor
        AnnouncementFilter.IMPORTANT -> importantColor
    }
    
    return if(isSelected){
        AnnouncementFilterColors(
            dotColor = dotColor,
            backgroundColor = MaterialTheme.colorScheme.primary,
            borderColor = Color.Transparent,
            titleColor = Color.White,
            countBackgroundColor = Color.White.copy(0.2f),
            countTextColor = Color.White
        )
    } else {
        AnnouncementFilterColors(
            dotColor = dotColor,
            backgroundColor = MaterialTheme.colorScheme.surface,
            borderColor = MaterialTheme.colorScheme.outline,
            titleColor = MaterialTheme.colorScheme.onSurfaceVariant,
            countBackgroundColor = MaterialTheme.colorScheme.tertiary,
            countTextColor = MaterialTheme.colorScheme.onTertiary
        )
    }
}
private data class AnnouncementFilterColors(
    val dotColor: Color,
    val backgroundColor: Color,
    val borderColor: Color,
    val titleColor: Color,
    val countBackgroundColor: Color,
    val countTextColor: Color
)

@PreviewLightDark
@Composable
fun previewFilterTabs(){
    KubikTheme() {
        AnnouncementFilterTabs(
            filters = listOf(
                AnnouncementFilter.ALL,
                AnnouncementFilter.NORMAL,
                AnnouncementFilter.IMPORTANT
            ),
            selectedFilter = AnnouncementFilter.NORMAL,
            onFilterChange = {},
            10,
            6,
            4
        )
    }
}