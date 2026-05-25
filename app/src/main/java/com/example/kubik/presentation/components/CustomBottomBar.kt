package com.example.kubik.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.kubik.presentation.navigation.NavigationItem
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme
import com.example.kubik.presentation.theme.Surface
import com.example.kubik.presentation.theme.glow

@Composable
fun CustomBottomBar(
    items: List<NavigationItem>,
    currentRoute: String?,
    onItemClick: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
){
    val isDarkTheme = LocalIsDarkTheme.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = RoundedCornerShape(36.dp),
            color = if(isDarkTheme) Color(0xFF0F172B).copy(0.8f) else Color(0xFFFFFFFF).copy(0.8f),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline
            ),
//            tonalElevation = 8.dp,
            shadowElevation = 12.dp
        ){
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                items.forEach { item ->
                    CustomBottomBarItem(
                        item = item,
                        selected = isBottomBarItemSelected(
                            item = item,
                            currentRoute = currentRoute
                        ),
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}

private fun isBottomBarItemSelected(
    item: NavigationItem,
    currentRoute: String?
): Boolean {
    if (currentRoute == null) return false
    if (currentRoute == item.route) return true
    return when (item) {
        NavigationItem.Queues -> {
            currentRoute.contains("queue", ignoreCase = true)
        }
        NavigationItem.Files -> {
            currentRoute.contains("file", ignoreCase = true)
        }
        NavigationItem.Requests -> {
            currentRoute.contains("request", ignoreCase = true) ||
                    currentRoute.contains("question", ignoreCase = true)
        }
        NavigationItem.Home -> {
            currentRoute.contains("home", ignoreCase = true)
        }
        NavigationItem.Calendar -> {
            currentRoute.contains("calendar", ignoreCase = true)
        }
        else -> false
    }
}

@Composable
private fun CustomBottomBarItem(
    item: NavigationItem,
    selected: Boolean,
    onClick: () -> Unit
){
    val isDarkTheme = LocalIsDarkTheme.current
    val iconColor = if(selected){
        if(isDarkTheme) Color(0xFFA3B3FF) else Color(0xFF4F39F6)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val iconOffsetY by animateDpAsState(
        targetValue = if(selected) (-2).dp else 0.dp,
        animationSpec = tween(220),
        label = "bottomBarIconOffset"
    )
    val dotSize by animateDpAsState(
        targetValue = if(selected) 6.dp else 0.dp,
        animationSpec = tween(220),
        label = "bottomBarDotSize"
    )
    Box(
        modifier = Modifier
            .size(56.dp)
            .then(
                if (selected) {
                    Modifier.shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                        clip = false
                    )
                } else {
                    Modifier
                }
            )
            .clip(CircleShape)
            .background(
                color = if(selected){
                    if(isDarkTheme) Color(0xFF191D4C) else Color(0xFFFFFFFF)
                } else {
                    Color.Transparent
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if(item.icon != null){
                Icon(
                    item.icon,
                    contentDescription = item.title,
                    tint = iconColor,
                    modifier = Modifier.size(26.dp)
                        .offset{
                            IntOffset(0, iconOffsetY.roundToPx())
                        }
                )
            } else if (item.iconId != null){
                Icon(
                    painter = painterResource(item.iconId),
                    contentDescription = item.title,
                    tint = iconColor,
                    modifier = Modifier.size(26.dp)
                        .offset{
                            IntOffset(0, iconOffsetY.roundToPx())
                        }
                )
            }
            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            )
        }
    }
}


@PreviewLightDark
@Composable
fun PreviewCustomBottomBar() {
    KubikTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            CustomBottomBar(
                items = listOf(
                    NavigationItem.Calendar,
                    NavigationItem.Queues,
                    NavigationItem.Home,
                    NavigationItem.Requests,
                    NavigationItem.Files
                ),
                currentRoute = NavigationItem.Home.route,
                onItemClick = {}
            )
        }
    }
}