package com.example.kubik.presentation.group.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.presentation.theme.KubikAttentionDark
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun SegmentedTab(
    modifier: Modifier,
    isSelected: Boolean,
    icon: Painter,
    count: Int,
    title: String,
    onClick: () -> Unit
){
    val backgroundColor by animateColorAsState(
        targetValue =
            if (isSelected)
                MaterialTheme.colorScheme.surfaceVariant
            else MaterialTheme.colorScheme.surfaceVariant.copy(0f),
        animationSpec = tween(200),
        label = "background_color_animation"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(200),
        label = "content_color_animation"
    )
    Surface(
        modifier = modifier.height(44.dp),
        color = backgroundColor,
        shape = RoundedCornerShape(14.dp),
        onClick = onClick,
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = "Иконка",
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                title,
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_semibold,
                        FontWeight.SemiBold
                    )
                ),
                lineHeight = 14.sp,
                color = contentColor,
                modifier = Modifier.padding(start = 8.dp).offset(y = (-1).dp),

            )
            if(count > 0){
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .background(
                            color = if(title == "Заявки") KubikAttentionDark else MaterialTheme.colorScheme.secondaryContainer,
                            shape = CircleShape
                        )
                        .width(24.dp)
                        .height(18.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        count.toString(),
                        fontSize = 10.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.inter_semibold,
                                FontWeight.SemiBold
                            )
                        ),
                        color = if(title == "Заявки") Color.White else contentColor,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            platformStyle =  PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }
            }

        }
    }
}

@PreviewLightDark
@Composable
fun SegmentedTabPreview(){
    KubikTheme() {
        SegmentedTab(
            Modifier,
            false,
            painterResource(R.drawable.applications),
            3,
            "Студенты",
            {}
        )
    }
}