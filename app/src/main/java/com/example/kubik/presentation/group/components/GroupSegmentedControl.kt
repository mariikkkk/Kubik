package com.example.kubik.presentation.group.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun GroupSegmentedControl(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    studentsCount: Int,
    requestsCount: Int
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        SegmentedTab(
            modifier = Modifier.weight(1f),
            selectedTab == 0,
            painterResource(R.drawable.group),
            studentsCount,
            "Студенты",
            { onTabSelected(0) }
        )
        SegmentedTab(
            modifier = Modifier.weight(1f),
            selectedTab == 1,
            painterResource(R.drawable.applications),
            requestsCount,
            "Заявки",
            { onTabSelected(1) }
        )
    }

}

@PreviewLightDark
@Composable
fun GroupSegmentedControlPreview(){
    KubikTheme() {
        GroupSegmentedControl(1, {0 }, 1, 2)
    }
}