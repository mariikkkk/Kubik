package com.example.kubik.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.kubik.presentation.queues.QueueFilter
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun <T: Enum<T>>FilterTabs(
    entries: List<T>,
    selectedItem: T,
    onItemClick: (T) -> Unit,
    itemTitle: (T) -> String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        entries.forEach { categoryEnum ->
            val isSelected = selectedItem == categoryEnum

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface
                    )
                    .clickable { onItemClick(categoryEnum) }
                    .border(
                        if (isSelected) -1.dp
                        else 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center

            )
            {
                Text(
                    itemTitle(categoryEnum),
                    color = if (isSelected) Color.White
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
            Spacer(Modifier.width(8.dp))

        }
    }
}

@PreviewLightDark
@Composable
fun previewFilterTab(){
    KubikTheme() {
        FilterTabs(
            entries = QueueFilter.entries,
            selectedItem = QueueFilter.ALL,
            onItemClick = {},
            itemTitle = { filter ->
                when(filter) {
                    QueueFilter.ALL -> "Все"
                    QueueFilter.ACTIVE -> "Активные"
                    QueueFilter.MINE -> "Мои"
                    QueueFilter.CLOSED -> "Завершенные"
                }
            }
        )
    }
}