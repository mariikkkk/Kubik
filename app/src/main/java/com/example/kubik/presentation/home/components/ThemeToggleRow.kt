package com.example.kubik.presentation.home.components

import android.content.res.Resources
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.domain.models.ThemeMode
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun ThemeToggleRow(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            "Тема",
            fontFamily = FontFamily(
                Font(
                    R.font.inter_medium,
                    FontWeight.Medium
                )
            ),
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(4.dp)
        ){
            ThemeIconButton(
                icon = painterResource(R.drawable.light_theme),
                isSelected = currentTheme == ThemeMode.LIGHT,
                onClick = { onThemeChange(ThemeMode.LIGHT) }
            )
            ThemeIconButton(
                icon = painterResource(R.drawable.system_theme),
                isSelected = currentTheme == ThemeMode.SYSTEM,
                onClick = { onThemeChange(ThemeMode.SYSTEM) }
            )
            ThemeIconButton(
                icon = painterResource(R.drawable.dark_theme),
                isSelected = currentTheme == ThemeMode.DARK,
                onClick = { onThemeChange(ThemeMode.DARK) }
            )
        }
    }
}

@Composable
fun ThemeIconButton(
    icon: Painter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if(isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
        label = "bg_anim"
    )
    val iconColor by animateColorAsState(
        targetValue = if(isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
        label = "icon_anim"
    )
    val shadowElevation by animateDpAsState(
        targetValue = if(isSelected) 8.dp else 0.dp,
        label = "shadow_anim"
    )

    Box(
        modifier = Modifier
            .size(36.dp)
            .shadow(
                elevation = shadowElevation,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0xFF5E44FF),
                ambientColor = Color(0xFF5E44FF)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable{ onClick() },
        contentAlignment = Alignment.Center
    ){
        Icon(
            icon,
            contentDescription = "Light theme",
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
    }
}


@PreviewLightDark
@Composable
fun PreviewThemeToggleRow(){
    KubikTheme() {
        ThemeToggleRow(currentTheme = ThemeMode.LIGHT, onThemeChange = {})
    }

    }
