package com.example.kubik.presentation.queues.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kubik.R
import com.example.kubik.presentation.components.CustomTextField
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.glow
import com.example.kubik.presentation.utils.toFormattedDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueCreateDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, totalSlots: Int, submissionDate: Long?) -> Unit
){
    var title by remember { mutableStateOf("") }
    var totalSlots by remember { mutableStateOf("10") }
    var submissionDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
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
                "Новая очередь",
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
                        "День сдачи",
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(32.dp)
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer,
                                RoundedCornerShape(14.dp)
                            )
                            .border(
                                1.dp,
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.outline
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .clickable { showDatePicker = true},
                        contentAlignment = Alignment.CenterStart
                    ){
                        Text(
                            submissionDate?.toFormattedDate("d'.'MM'.'uuuu") ?: "ДД.ММ.ГГГГ",
                            color = if(showDatePicker) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 16.sp
                        )
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = "Выбрать день сдачи",
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.CenterEnd),
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        "Мест",
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
                        { totalSlots = it },
                        "Введите количество мест",
                        fontSize = 16.sp,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    val slots = totalSlots.toIntOrNull() ?: return@Button
                    if(title.isNotBlank() && slots in 1..1000 && submissionDate != null){
                        onCreate(title, slots, submissionDate)
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
                    "Создать очередь",
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
    if(showDatePicker){
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false},
            confirmButton = {
                TextButton(
                    onClick = {
                        submissionDate = datePickerState.selectedDateMillis
                        showDatePicker = false
                    }
                ) {
                    Text("Выбрать")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false}
                ) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(datePickerState)
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewQueueCreateDialog(){
    KubikTheme() {
        QueueCreateDialog(
            {},
            {s, i, l ->}
        )
    }
}