package com.example.kubik.presentation.queues.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.kubik.R
import com.example.kubik.presentation.components.CustomTextField
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.LocalIsDarkTheme
import com.example.kubik.presentation.theme.glow

@Composable
fun QueueCloseDialog(
    onDismiss: () -> Unit,
    onMigrate: (Int) -> Unit,
    onClose: () -> Unit,
    totalSlots: Int,
    waitingStudents: Int
){
    var totalSlotsText by remember(totalSlots) { mutableStateOf(totalSlots.toString()) }
    val isDarkTheme = LocalIsDarkTheme.current
    val closeColorContainer = if(isDarkTheme) Color(0xFF1A1D46) else Color(0xFFE0E7FF)
    val closeColorContent = Color(0xFF615FFF)
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface
        ){
            Column( modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            shape = CircleShape,
                            color = closeColorContainer
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(R.drawable.close_queue),
                        contentDescription = "Предупреждение перед перезаписью",
                        modifier = Modifier.size(30.dp).offset(y=(-2).dp),
                        tint = closeColorContent

                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Сдача окончена",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "В очереди остались несдавшие студенты ($waitingStudents чел). " +
                            "Хотите создать новую очередь и перенести их автоматически?",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Text(
                        "Количество мест",
                        fontFamily = FontFamily(
                            Font(
                                R.font.inter_medium,
                                FontWeight.Medium
                            )
                        ),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.height(4.dp))
                    CustomTextField(
                        totalSlotsText,
                        { value ->
                            totalSlotsText = value.filter { it.isDigit() }
                        },
                        "Введите количество мест",
                        fontSize = 16.sp,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
                Column (modifier = Modifier.fillMaxWidth()){
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val newTotalSlots = totalSlotsText.toIntOrNull() ?: return@Button
                            if (newTotalSlots !in 1..1000) return@Button
                            onMigrate(newTotalSlots)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .glow(
                                closeColorContent,
                                alpha = 0.5f,
                                5.dp,
                                20.dp
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = closeColorContent
                        )
                    ) {
                        Text(
                            text = "Создать новую",
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_semibold,
                                    FontWeight.SemiBold
                                )
                            ),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {
                            onClose()
                            onDismiss()
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ){
                        Text(
                            "Просто завершить",
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_semibold,
                                    FontWeight.SemiBold
                                )
                            ),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ){
                        Text(
                            text = "Отмена",
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_semibold,
                                    FontWeight.SemiBold
                                )
                            ),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun previewQueueCloseDialog(){
    KubikTheme() {
        QueueCloseDialog(
            {},
            { _ -> },
            {},
            2,
            5
        )
    }
}