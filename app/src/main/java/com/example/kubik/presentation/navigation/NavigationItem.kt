package com.example.kubik.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.kubik.R

sealed class NavigationItem(val route: String, val icon: ImageVector? = null, val iconId: Int? = null, val title: String) {
    object Home    : NavigationItem("home", iconId = R.drawable.home, title = "Главная")
    object Queues  : NavigationItem("queues", iconId = R.drawable.queues, title = "Очереди")
    object Calendar: NavigationItem("calendar", iconId = R.drawable.deadline, title = "Календарь")
    object Requests: NavigationItem("questions", iconId = R.drawable.questions, title = "Вопросы")
    object Files   : NavigationItem("files", iconId = R.drawable.files, title = "Файлы")
    object QueueDetails: NavigationItem("queue_details/{queueId}", title = "Очередь"){
        fun route(queueId: String) = "queue_details/$queueId"
    }
    object Announcements : NavigationItem("announcements", title = "Объявления")

}