package com.example.kubik.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.domain.models.ThemeMode
import com.example.kubik.domain.models.User
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun CustomDrawerContent(
    user: User?,
    groupName: String,
    onLogoutClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onThemeChange: (ThemeMode) -> Unit,
    currentTheme: ThemeMode
){
    val isDarkTheme = when(currentTheme){
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    val role = when(user?.role){
        "student" -> "Студент"
        "starosta" -> "Староста"
        else -> "Загрузка..."
    }
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.primaryContainer,
        drawerContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.width(300.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
        ){
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(brush = Brush.linearGradient(listOf(Color(0xFF615FFF), Color(0xFF9810FA)))),
                contentAlignment = Alignment.Center
            ){
                Text(
                    (user?.firstName ?: "С").take(1),
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_bold,
                            FontWeight.Bold
                        )
                    ),
                    fontSize = 24.sp,
                    color = Color.White
                    )
            }
            Spacer(Modifier.height(12.dp))
            Text("${user?.firstName ?: "Загрузка..."} ${user?.lastName ?: ""}",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_bold,
                        FontWeight.Bold
                    )
                ),
                fontSize = 20.sp
            )
            Text(
                "$groupName • ${role }",
                modifier = Modifier.padding(start = 16.dp),
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_regular,
                        FontWeight.Normal
                    )
                ),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(24.dp))
            HorizontalDivider(
                modifier = Modifier.width(300.dp),
                thickness = 1.dp,
                color = Color(0xFf1f5f9)
            )
            Spacer(Modifier.height(16.dp))
            DrawMenuItem(
                "Список группы",
                painterResource(R.drawable.group),
                {},
                "2"
            )
            DrawMenuItem(
                "Настройки профиля",
                painterResource(R.drawable.settings),
                onSettingsClick
            )
            ThemeToggleRow(currentTheme, onThemeChange)

            Spacer(Modifier.weight(1f))
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(16.dp)
                    .border(
                        width = 1.dp,
                        color = if(isDarkTheme) Color(0xFFEC003F) else Color(0xFFFF637E),
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor =
                    if(isDarkTheme) Color(0xFFFF2056).copy(alpha = 0.1f) else Color(0xffFFF1F2))
            ){
                Icon(
                    painter = painterResource(R.drawable.exit),
                    contentDescription = "Выйти из аккаунта",
                    tint = if(isDarkTheme) Color(0xFFEC003F) else Color(0xFFFF637E)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Выйти из аккаунта",
                    color = if(isDarkTheme) Color(0xFFEC003F) else Color(0xFFFF637E),
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_bold,
                            FontWeight.Bold
                        )
                    ),)

            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewCustomDrawerContent(){
    KubikTheme() {
        val isDarkTheme by remember { mutableStateOf(ThemeMode.DARK) }
        CustomDrawerContent(
            User("1","Марат", "Цой"),
            "ИКБО-31-24",
            {},
            {},
            {},
            currentTheme = isDarkTheme
        )
    }

}