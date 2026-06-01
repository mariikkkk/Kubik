package com.example.kubik.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun QuickActionItem(
    icon: Int,
    title: String,
    backgroundColor: Color,
    iconTint: Color,
    onClick: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .size(54.dp)
                .clickable{
                    onClick()
                },
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(icon),
                contentDescription = "Значок кнопки",
                modifier = Modifier.size(24.dp),
                tint = iconTint
            )
        }
        Text(
            text = title,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@PreviewLightDark
@Composable
fun PreviewActionItem(){
    KubikTheme() {
        QuickActionItem(
            R.drawable.notifications,
            "Объявления",
            Color(0xFF0B0E2E),
            Color(0xFF615FFF),
            onClick = {  }
        )
    }
}