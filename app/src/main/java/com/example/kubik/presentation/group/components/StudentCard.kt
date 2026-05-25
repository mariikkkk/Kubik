package com.example.kubik.presentation.group.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.domain.models.User
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme

@Composable
fun StudentCard(
    user: User,
    isStarosta: Boolean,
    number: Int,
    onKickClick: () -> Unit
){
    val isDarkTheme = LocalIsDarkTheme.current
    var studentIdToDelete by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val role = when(user?.role){
        "student" -> "Студент"
        "starosta" -> "Староста"
        else -> "Загрузка..."
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                number.toString(),
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_bold,
                        FontWeight.Bold
                    )
                ),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = user.firstName[0].toString(),
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_bold,
                            FontWeight.Bold
                        )
                    ),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column() {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    fontFamily = FontFamily(
                        Font(
                            R.font.inter_semibold,
                            FontWeight.SemiBold
                        )
                    ),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .widthIn(68.dp)
                        .background(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                        .padding(start = 12.dp, end = 12.dp),
                    contentAlignment = Alignment.Center

                ){
                    Text(
                        text = role,
                        fontFamily = FontFamily(
                            Font(
                                R.font.inter_medium,
                                FontWeight.Medium
                            )
                        ),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            }
            if(isStarosta){
                Spacer(modifier = Modifier.weight(1f))
                Box(){
                    IconButton(
                        onClick = { expanded = true}
                    ) {
                        Icon(Icons.Default.MoreVert,
                            contentDescription = "Меню",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            { Text("Исключить") },
                            onClick = {
                                expanded = false
                                studentIdToDelete = user.id
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.DeleteOutline,
                                    contentDescription = "Исключить студента из группы",
                                    tint = Color.Red
                                    )
                            }

                        )
                    }
                }

            }
        }
    }
    if(studentIdToDelete != null){
        CustomExcludeUserDialog(
            isDarkTheme,
            user.firstName,
            user.lastName,
            { studentIdToDelete = null },
            onConfirm = {
                onKickClick()
            }
        )
    }
}

@PreviewLightDark
@Composable
fun StudentCardPreview(){
    KubikTheme() {
        StudentCard(
            User("1", "Марат", "Цой", "Староста", "starosta"),
    true,
            2,
             {})}

}
