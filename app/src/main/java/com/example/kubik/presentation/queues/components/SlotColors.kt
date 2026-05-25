package com.example.kubik.presentation.queues.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.kubik.domain.queues.models.SlotStatus
import com.example.kubik.presentation.queues.SlotDisplayItem

data class SlotColors(
    val backgroundColor: Color,
    val textColor: Color,
    val borderColor: Color,
    val circleBackgroundColor: Color,
    val circleTextColor: Color
)

@Composable
fun SlotDisplayItem.resolveColors(isDarkTheme: Boolean): SlotColors {
    return when{
        isCurrentUser && slot?.typedStatus == SlotStatus.WAITING -> SlotColors(
            backgroundColor = Color(0xFF4F39F6),
            textColor = Color(0xFFFFFFFF),
            borderColor = Color(0xFF4F39F6),
            circleBackgroundColor = Color(0xFF7261F8),
            circleTextColor = Color(0xFFFFFFFF)
        )
        isActiveSlot -> SlotColors(
            backgroundColor = if(isDarkTheme) Color(0xFF1A0F14) else Color(0xFFFFFBEB),
            textColor = if(isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172B),
            borderColor = if(isDarkTheme) Color(0xFF51240C) else Color(0xFFFEE685),
            circleBackgroundColor = if(isDarkTheme) Color(0xFF314158) else Color(0xFFE2E8F0),
            circleTextColor = if(isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172B)
        )
        !isCurrentUser && slot?.typedStatus == SlotStatus.WAITING -> SlotColors(
            backgroundColor = if(isDarkTheme) Color(0xFF0B1124) else Color(0xFFF8FAFC),
            textColor = if(isDarkTheme) Color(0xFF62748E) else Color(0xFF62748E),
            borderColor = if(isDarkTheme) Color(0xFF0D1428) else Color(0xFFF1F5F9),
            circleBackgroundColor = if(isDarkTheme) Color(0xFF314158) else Color(0xFFE2E8F0),
            circleTextColor = if(isDarkTheme) Color(0xFF62748E) else Color(0xFF62748E)
        )
        slot?.typedStatus == SlotStatus.PASSED -> SlotColors(
            backgroundColor = if(isDarkTheme) Color(0xFF004F3B).copy(0.2f) else Color(0xFFECFDF5),
            textColor = if(isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172B),
            borderColor = if(isDarkTheme) Color(0xFF006045).copy(0.5f) else Color(0xFFA4F4CF),
            circleBackgroundColor = if(isDarkTheme) Color(0xFF314158) else Color(0xFFE2E8F0),
            circleTextColor = if(isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172B)
        )
        slot?.typedStatus == SlotStatus.FAILED -> SlotColors(
            backgroundColor = if(isDarkTheme) Color(0xFF8B0836).copy(0.2f) else Color(0xFFFFF1F2),
            textColor = if(isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172B),
            borderColor = if(isDarkTheme) Color(0xFFA50036).copy(0.5f) else Color(0xFFFFCCD3),
            circleBackgroundColor = if(isDarkTheme) Color(0xFF314158) else Color(0xFFE2E8F0),
            circleTextColor = if(isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172B)
        )
        else -> SlotColors(
            backgroundColor = if(isDarkTheme) Color(0xFF0F172B) else Color(0xFFFFFFFF),
            textColor = if(isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172B),
            borderColor = if(isDarkTheme) Color(0xFF314158).copy(0.5f) else Color(0xFFE2E8F0),
            circleBackgroundColor = if(isDarkTheme) Color(0xFF314158) else Color(0xFFE2E8F0),
            circleTextColor = if(isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172B)
        )
    }
}