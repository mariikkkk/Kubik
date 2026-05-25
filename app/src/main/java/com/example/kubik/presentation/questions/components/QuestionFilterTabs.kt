package com.example.kubik.presentation.questions.components

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
import com.example.kubik.presentation.questions.QuestionFilter
import com.example.kubik.presentation.theme.KubikTheme

private data class QuestionFilterColors(
    val dotColor: Color,
    val backgroundColor: Color,
    val borderColor: Color,
    val titleColor: Color,
    val countBackgroundColor: Color,
    val countTextColor: Color
)
@Composable
fun QuestionFilterTabs(
    filters: List<QuestionFilter>,
    selectedFilter: QuestionFilter,
    onFilterChange: (QuestionFilter) -> Unit,
    allCount: Int,
    waitingCount: Int,
    answeredCount: Int,
    resolvedCount: Int,
    mineCount: Int,
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
            QuestionFilterChip(
                title = getQuestionFilterTitle(filter),
                count = getQuestionFilterCount(
                    filter = filter,
                    allCount = allCount,
                    waitingCount = waitingCount,
                    answeredCount = answeredCount,
                    resolvedCount = resolvedCount,
                    mineCount = mineCount
                ),
                isSelected = isSelected,
                questionFilterColors = getQuestionFilterColors(
                    filter = filter,
                    isSelected = isSelected
                ),
                onClick = {
                    onFilterChange(filter)
                }
            )
        }
    }
}

@Composable
private fun QuestionFilterChip(
    title: String,
    count: Int,
    isSelected: Boolean,
    questionFilterColors: QuestionFilterColors,
    onClick: () -> Unit
){
    Box(
        modifier = Modifier
            .background(
                color = questionFilterColors.backgroundColor,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = if(isSelected) (-1).dp else 1.dp,
                color = questionFilterColors.borderColor,
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
                        color = questionFilterColors.dotColor,
                        shape = CircleShape
                    )
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = title,
                color = questionFilterColors.titleColor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Medium
            )
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = questionFilterColors.countBackgroundColor,
                        shape = CircleShape
                    )
                    .width(24.dp)
                    .height(18.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    count.toString(),
                    color = questionFilterColors.countTextColor,
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

@Composable
private fun getQuestionFilterColors(
    filter: QuestionFilter,
    isSelected: Boolean
): QuestionFilterColors{
    val waitingColor = Color(0xFFFE9A00)
    val answeredColor = Color(0xFF2B7FFF)
    val resolvedColor = Color(0xFF00BC7D)
    val mineColor = MaterialTheme.colorScheme.primary
    val allColor = Color(0xFF90A1B9)
    val dotColor = when(filter){
        QuestionFilter.ALL -> allColor
        QuestionFilter.WAITING -> waitingColor
        QuestionFilter.ANSWERED -> answeredColor
        QuestionFilter.RESOLVED -> resolvedColor
        QuestionFilter.MINE -> mineColor
    }
    return if(isSelected){
        QuestionFilterColors(
            dotColor = dotColor,
            backgroundColor = MaterialTheme.colorScheme.primary,
            borderColor = Color.Transparent,
            titleColor = Color.White,
            countBackgroundColor = Color.White.copy(0.2f),
            countTextColor = Color.White
        )
    }else{
        QuestionFilterColors(
            dotColor = dotColor,
            backgroundColor = MaterialTheme.colorScheme.surface,
            borderColor = MaterialTheme.colorScheme.outline,
            titleColor = MaterialTheme.colorScheme.onSurfaceVariant,
            countBackgroundColor = MaterialTheme.colorScheme.tertiary,
            countTextColor = MaterialTheme.colorScheme.onTertiary
        )
    }
}

private fun getQuestionFilterTitle(filter: QuestionFilter): String{
    return when(filter){
        QuestionFilter.ALL -> "Все"
        QuestionFilter.WAITING -> "Ожидают"
        QuestionFilter.ANSWERED -> "Есть ответ"
        QuestionFilter.RESOLVED -> "Решено"
        QuestionFilter.MINE -> "Мои"
    }
}
private fun getQuestionFilterCount(
    filter: QuestionFilter,
    allCount: Int,
    waitingCount: Int,
    answeredCount: Int,
    resolvedCount: Int,
    mineCount: Int
): Int{
    return when(filter){
        QuestionFilter.ALL -> allCount
        QuestionFilter.WAITING -> waitingCount
        QuestionFilter.ANSWERED -> answeredCount
        QuestionFilter.RESOLVED -> resolvedCount
        QuestionFilter.MINE -> mineCount
    }
}
@PreviewLightDark
@Composable
fun previewQuestionFilterTabs() {
    KubikTheme {
        QuestionFilterTabs(
            filters = listOf(
                QuestionFilter.ALL,
                QuestionFilter.WAITING,
                QuestionFilter.ANSWERED,
                QuestionFilter.RESOLVED,
                QuestionFilter.MINE
            ),
            selectedFilter = QuestionFilter.WAITING,
            onFilterChange = {},
            allCount = 10,
            waitingCount = 2,
            answeredCount = 3,
            resolvedCount = 4,
            mineCount = 1
        )
    }
}