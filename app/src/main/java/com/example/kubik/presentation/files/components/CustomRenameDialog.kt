package com.example.kubik.presentation.files.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.kubik.R
import com.example.kubik.presentation.components.CustomTextField
import com.example.kubik.presentation.theme.KubikTheme
import com.example.kubik.presentation.theme.glow

@Composable
fun CustomRenameDialog(
    fileName: String,
    onDismiss: () -> Unit,
    onRenameClick: (String) -> Unit
){
    val extension = remember(fileName){
        val dotIndex = fileName.lastIndexOf('.')
        if(dotIndex != -1) fileName.substring(dotIndex) else ""
    }
    var nameWithoutExtension = remember(fileName){
        mutableStateOf(fileName.dropLast(extension.length))
    }

    Dialog(
        onDismissRequest = onDismiss
    ){
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outline
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Переименовать",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.inter_bold
                            )
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(
                        onClick = onDismiss
                    ) {
                        Icon(Icons.Default.Close,
                            contentDescription = "Закрыть окно",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(Modifier.height(8.dp))
                CustomTextField(
                    nameWithoutExtension.value,
                    { nameWithoutExtension.value = it },
                    "Новое имя файла",
                    fontSize = 16.sp,
                    modifier = Modifier.height(49.dp)
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { onRenameClick(nameWithoutExtension.value + extension) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .glow(
                            MaterialTheme.colorScheme.primary,
                            0.5f,
                            16.dp,
                            32.dp)
                    ,
                    colors = buttonColors(
                        disabledContainerColor = MaterialTheme.colorScheme.primary,
                        disabledContentColor = Color.Gray,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = nameWithoutExtension.value.isNotBlank(),
                ) {
                    Text(
                        "Сохранить",
                        fontFamily = FontFamily(
                            Font(
                                R.font.inter_semibold,
                                FontWeight.SemiBold
                            )
                        ),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun previewRenameDialog(){
    KubikTheme() {
        CustomRenameDialog(fileName = "", onDismiss = {}, onRenameClick = {})

    }
}