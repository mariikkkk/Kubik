package com.example.kubik.presentation.group.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme

@Composable
fun GroupCard(
    isStarosta: Boolean,
    groupName: String,
    iconLight: Painter,
    iconDark: Painter
){
    val isDarkTheme = LocalIsDarkTheme.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(105.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text(
                    "Группа $groupName",
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_bold,
                            FontWeight.Bold
                        )
                    ),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    if(isStarosta) {
                        "Панель управления старосты"
                    }else{
                        "Список участников группы"
                    },
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_regular,
                            FontWeight.Normal
                        )
                    ),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.weight(1f))
            Icon(
                painter = if(isDarkTheme) iconDark else iconLight,
                contentDescription = "Иконка роли",
                modifier = Modifier.size(64.dp),
                tint = Color.Unspecified
            )
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewGroupCard(){
    KubikTheme() {
        GroupCard(
            true,
            "ИКБО-31-24",
            painterResource(R.drawable.starosta_light),
            painterResource(R.drawable.starosta_dark)
        )
    }
}