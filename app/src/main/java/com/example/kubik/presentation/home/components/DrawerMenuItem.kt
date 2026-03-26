package com.example.kubik.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R

@Composable
fun DrawMenuItem(
    title: String,
    icon: Painter,
    onClick: () -> Unit,
    badgeText: String? = null
){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ){
        Row(
            Modifier
                .fillMaxWidth()
        ){
            Icon(
                painter = icon,
                contentDescription = title,
            )
            Spacer(Modifier.width(16.dp))
            Text(
                title,
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_medium,
                        FontWeight.Normal
                    )
                ),
                fontSize = 16.sp,
            )
            if (badgeText != null){
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .sizeIn(24.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFF2056)),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        badgeText,
                        color = Color.White)
                }
            }
        }
    }
}