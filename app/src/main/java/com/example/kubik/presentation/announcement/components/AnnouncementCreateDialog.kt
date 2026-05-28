package com.example.kubik.presentation.announcement.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.kubik.R
import com.example.kubik.domain.announcement.model.AnnouncementType
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun AnnouncementCreateDialog(
    title: String,
    text: String,
    selectedType: AnnouncementType,
    onTitleChange: (String) -> Unit,
    onTextChange: (String) -> Unit,
    onTypeChange: (AnnouncementType) -> Unit,
    onDismissClick: () -> Unit,
    onCreateClick: () -> Unit
){
    Dialog(
        onDismissRequest = onDismissClick
    ){
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ){
            Column(
                modifier = Modifier.padding(25.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Новое объявление",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily(
                            Font(
                                R.font.inter_bold,
                                FontWeight.Bold
                            )
                        ),
                        fontSize = 20.sp
                    )
                    IconButton(
                        onClick = onDismissClick
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Закрыть окно создания объявления")
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Заголовок",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(R.font.inter_medium)
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = title,
                    onValueChange = onTitleChange,
                    placeHolder = "Например: Перенос пар",
                    minHeight = 20
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "Приоритет",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontFamily = FontFamily(
                        Font(R.font.inter_medium)
                    )
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    AnnouncementTypeChip(
                        text = "Обычное",
                        iconId = R.drawable.default_announcement,
                        isSelected = selectedType == AnnouncementType.NORMAL,
                        onClick = { onTypeChange(AnnouncementType.NORMAL) }
                    )
                    AnnouncementTypeChip(
                        text = "Важное",
                        iconId = R.drawable.active,
                        isSelected = selectedType == AnnouncementType.IMPORTANT,
                        onClick = { onTypeChange(AnnouncementType.IMPORTANT) }
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    "Текст объявления",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontFamily = FontFamily(
                        Font(R.font.inter_medium)
                    )
                )
                Spacer(Modifier.height(10.dp))
                TextField(
                    text,
                    onTextChange,
                    "Подробности для студентов...",
                    100
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onCreateClick,
                    enabled = title.isNotBlank() && text.isNotBlank(),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline
                    ),
                    elevation = buttonElevation(
                        disabledElevation = 1.dp,
                        defaultElevation = 3.dp,
                        focusedElevation = 2.dp
                    ),
                    colors = buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.send),
                            contentDescription = "Отправить объявление",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Создать объявление",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(
                                Font(R.font.inter_semibold)
                            )
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: String,
    minHeight: Int
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(minHeight.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(18.dp)
            )
    ){
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 14.sp
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.padding(12.dp)
                ){
                    if(value.isEmpty()){
                        Text(
                            placeHolder,
                            color = Color(0xFF90A1B9),
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}
@Composable
private fun AnnouncementTypeChip(
    text: String,
    iconId: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    val backgroundColor = if(isSelected){
        MaterialTheme.colorScheme.primary
    } else{
        MaterialTheme.colorScheme.background
    }
    val contentColor = if(isSelected){
        Color.White
    } else{
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = "Иконка типа объявления",
            tint = contentColor,
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text,
            fontSize = 12.sp,
            fontFamily = FontFamily(
                Font(R.font.inter_medium)
            ),
            color = contentColor
        )
    }
}
@PreviewLightDark
@Composable
fun previewDialog() {
    KubikTheme {
        var title by remember { mutableStateOf("") }
        var text by remember { mutableStateOf("") }
        var selectedType by remember { mutableStateOf(AnnouncementType.NORMAL) }

        AnnouncementCreateDialog(
            title = title,
            text = text,
            selectedType = selectedType,
            onTitleChange = { title = it },
            onTextChange = { text = it },
            onTypeChange = { selectedType = it },
            onDismissClick = {},
            onCreateClick = {}
        )
    }
}