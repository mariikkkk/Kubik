package com.example.kubik.presentation.queues.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.presentation.components.CustomTextField
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.glow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueEditDialog(
    initialTitle: String,
    initialTotalSlots: Int,
    onDismiss: () -> Unit,
    onEdit: (String, Int) -> Unit
){
    var title by remember(initialTitle) { mutableStateOf(initialTitle) }
    var totalSlots by remember(initialTotalSlots) { mutableStateOf(initialTotalSlots.toString()) }
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ){
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 32.dp)
        ) {
            Text(
                "Редактировать очередь",
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_bold,
                        FontWeight.Bold
                    )
                ),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Название",
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
                title,
                { title = it },
                "Введите название очереди",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            )
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
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
                        totalSlots,
                        { value -> totalSlots =  value.filter { it.isDigit() } },
                        "Введите количество мест",
                        fontSize = 16.sp,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                "Уменьшение количества возможно только если нет занятых мест в конце очереди",
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_regular,
                        FontWeight.Normal
                    )
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    val slots = totalSlots.toIntOrNull()
                    if(slots == null || slots <= 0 || slots > 1000) return@Button

                    if(title.trim().isNotBlank()){
                        onEdit(title.trim(), slots)
                        onDismiss()
                    }
                },
                Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .glow(
                        MaterialTheme.colorScheme.primary,
                        0.5f,
                        5.dp,
                        30.dp
                    ),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "Сохранить изменения",
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
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewQueueEditDialog(){
    KubikTheme() {
        QueueEditDialog(
            initialTitle = "Защита лабораторной",
            initialTotalSlots = 10,
            onDismiss = {},
            onEdit = { _, _ -> }
        )
    }
}