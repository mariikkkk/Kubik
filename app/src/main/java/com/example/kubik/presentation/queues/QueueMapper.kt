package com.example.kubik.presentation.queues

import androidx.compose.ui.graphics.Color
import com.example.kubik.domain.queues.models.QueueItem
import com.example.kubik.presentation.theme.statusGreenSurface
import com.example.kubik.presentation.theme.statusGreenText
import com.example.kubik.presentation.theme.statusRedSurface
import com.example.kubik.presentation.theme.statusRedText

fun QueueItem.getStatusTheme(): Pair<Color, Color>{
    return when (this.status){
        "Открыто" -> statusGreenSurface to statusGreenText
        "Закрыто" -> statusRedSurface to statusRedText
        else -> Color.Gray to Color.DarkGray

    }
}