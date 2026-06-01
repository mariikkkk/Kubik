package com.example.kubik.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme

@Composable
fun QuickActionRow(
    onFileClick: () -> Unit,
    onAnnouncementClick: () -> Unit,
    onGroupClick: () -> Unit
){
    val isDarkTheme = LocalIsDarkTheme.current
    val colorBackgroundFile = if(isDarkTheme) Color(0xFF021520) else Color(0xFFECFDF5)
    val colorTintFile = Color(0xFF00bc7d)
    val colorBackgroundAnnouncement = if(isDarkTheme) Color(0xFF0B0E2E) else Color(0xFFEEF2FF)
    val colorTintAnnouncement = Color(0xFF615FFF)
    val colorBackgroundGroup = if(isDarkTheme) Color(0xFF14092F) else Color(0xFFFAF5FF)
    val colorTintGroup = Color(0xFFAD46FF)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier)
        QuickActionItem(
            icon = R.drawable.file,
            title = "Файлы",
            backgroundColor = colorBackgroundFile,
            iconTint = colorTintFile,
            onClick = onFileClick
        )
        QuickActionItem(
            icon = R.drawable.notifications,
            title = "Объявления",
            backgroundColor = colorBackgroundAnnouncement,
            iconTint = colorTintAnnouncement,
            onClick = onAnnouncementClick
        )
        QuickActionItem(
            icon = R.drawable.group,
            title = "Группа",
            backgroundColor = colorBackgroundGroup,
            iconTint = colorTintGroup,
            onClick = onGroupClick
        )
        Spacer(Modifier)
    }
}

@PreviewLightDark
@Composable
fun PreviewQuickActionRow(){
    KubikTheme() {
        QuickActionRow(
            {},
            {},
            {}
        )
    }
}