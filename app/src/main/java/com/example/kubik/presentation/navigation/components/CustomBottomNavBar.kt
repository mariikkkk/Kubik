package com.example.kubik.presentation.navigation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amazonaws.services.kms.model.KeyUnavailableException
import com.example.kubik.R
import com.example.kubik.presentation.navigation.NavigationItem
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun CustomBottomNavBar(
    items: List<NavigationItem>,
    currentRoute: String,
    onItemClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .fillMaxWidth()
            .height(60.dp)
            .clip(CircleShape)
            .background(Color(0xFF0F172B))
            .border(
                width = 1.dp,
                color = Color.White,
                shape = CircleShape
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = item.route == currentRoute
            val iconTint = if (isSelected) Color(0xFFA3B3FF) else Color.White
            val boxBackground = if (isSelected) Color(0xFF615FFF) else Color.Transparent

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(boxBackground)
                    .clickable { onItemClick(item.route) },
                contentAlignment = Alignment.Center

            ) {
                if (item.icon != null) {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = iconTint,
                        modifier = Modifier.size(26.dp)
                    )
                } else if (item.iconId != null) {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = item.title,
                        tint = iconTint,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomBottomNavBar() {
    // 1. Берем твой реальный список экранов
    val navItems = listOf(
        NavigationItem.Home,
        NavigationItem.Queues,
        NavigationItem.Calendar,
        NavigationItem.Requests,
        NavigationItem.Files
    )

    // 2. Имитируем нижнюю часть экрана приложения
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp) // Задаем небольшую высоту, чтобы увидеть плавающий эффект
            .background(Color(0xFF020618)), // Темно-синий фон твоего приложения
        contentAlignment = Alignment.BottomCenter
    ) {
        // 3. Вызываем сам бар
        CustomBottomNavBar(
            items = navItems,
            currentRoute = NavigationItem.Home.route, // Делаем кнопку "Главная" активной
            onItemClick = {} // В превью клики никуда не ведут, поэтому лямбда пустая
        )
    }
}
