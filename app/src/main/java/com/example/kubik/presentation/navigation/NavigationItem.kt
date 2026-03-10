package com.example.kubik.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.kubik.R

sealed class NavigationItem(val route: String, val icon: ImageVector? = null, val iconId: Int? = null, val title: String) {
    object Home    : NavigationItem("home_main", Icons.Default.Home, title = "Главная")
    object Queues  : NavigationItem("queues", Icons.Default.List, title = "Очереди")
    object Calendar: NavigationItem("calendar", Icons.Default.DateRange, title = "Календарь")
    object Requests: NavigationItem("requests", iconId = R.drawable.message, title = "Запросы")
    object Files   : NavigationItem("files", iconId = R.drawable.file, title = "Файлы")

}