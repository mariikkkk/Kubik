package com.example.studhub.presentation.files.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.studhub.domain.models.FileCategory

fun Modifier.dashedBorder(color: Color, cornerRadius: Dp) = composed {
    val density = LocalDensity.current
    val strokedWithPx = with(density) {1.dp.toPx()}
    val cornerRadiusPx = with (density) { cornerRadius.toPx() }
    this.then(
        Modifier.drawBehind{
            drawRoundRect(
                color = color,
                style = Stroke(
                    width = strokedWithPx,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                ),
                cornerRadius = CornerRadius(cornerRadiusPx)
            )
        }
    )
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomUploadDialog(
    folderName: String,
    folders: List<String>,
    onDismiss: () -> Unit,
    onUploadClick: (String, FileCategory, String) -> Unit
){
    var fileName by remember { mutableStateOf("") }
    var selectedCategory by remember {mutableStateOf(FileCategory.OTHER)}
    var expandedCategoryMenu by remember { mutableStateOf(false) }
    var expandedFolderMenu by remember { mutableStateOf(false) }
    var selectedFolder by remember { mutableStateOf(folderName) }

    Dialog(onDismissRequest = onDismiss){
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ){
            Column(modifier = Modifier.padding(24.dp)){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Column(modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Загрузка файла",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Поделитесь полезными материалами с группой",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )

                    }
                    IconButton(
                        onClick = onDismiss, Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть")
                    }

                }
                Spacer(Modifier.height(24.dp))
                DialogRow(label="Название"){
                    BasicTextField(
                        value = fileName,
                        onValueChange = {fileName = it},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(12.dp)
                            ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                if (fileName.isEmpty()){
                                    Text("Например: Лекция №1.pdf",
                                        color = Color.LightGray,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                innerTextField()
                            }


                        }

                    )
                }
                Spacer(Modifier.height(16.dp))
                DialogRow(label="Предмет"){
                    Box{
                        BasicTextField(
                            value = selectedFolder,
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                                .height(32.dp)
                                .padding(end = 12.dp),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            readOnly = false,
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ){
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ){
                                        Box(modifier = Modifier
                                            .weight(1f)
                                            .padding(start = 12.dp)
                                            .basicMarquee()){
                                            innerTextField()
                                        }
                                        if(folders.size > 1) {
                                            Icon(
                                                Icons.Default.KeyboardArrowDown,
                                                contentDescription = "Выпадающее меню предметов",
                                                modifier = Modifier.size(20.dp),
                                                tint = Color.Gray
                                            )
                                        }

                                    }
                                }
                            }

                        )
                        if (folders.size > 1){
                            Surface(
                                modifier = Modifier.matchParentSize(),
                                color = Color.Transparent,
                                onClick = { expandedFolderMenu = true }
                            ) {
                                DropdownMenu(
                                    expanded = expandedFolderMenu,
                                    onDismissRequest = { expandedFolderMenu = false },
                                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                                ) {
                                    folders.forEach { folderItem ->
                                        DropdownMenuItem(
                                            text = { Text(folderItem) },
                                            onClick = {
                                                selectedFolder = folderItem
                                                expandedFolderMenu = false

                                            }
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                DialogRow(label="Категория") {
                    Box{
                        BasicTextField(
                            value = selectedCategory.title,
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(12.dp))
                                .height(32.dp),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            decorationBox = { innerTextField ->
                                Box(modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center){
                                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween){
                                        Box(modifier = Modifier.weight(1f),){
                                            innerTextField()
                                        }
                                        Icon(
                                            Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Выпдающее меню категорий",
                                            modifier = Modifier.size(20.dp),
                                            tint = Color.Gray
                                        )
                                    }

                                }
                            }
                        )
                        Surface(
                            modifier = Modifier.matchParentSize(),
                            color = Color.Transparent,
                            onClick = { expandedCategoryMenu = true })
                        {

                        }
                        DropdownMenu(
                            expanded = expandedCategoryMenu,
                            onDismissRequest = { expandedCategoryMenu = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            FileCategory.entries.filter { it != FileCategory.ALL }.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.title) },
                                    onClick = {
                                        selectedCategory = category
                                        expandedCategoryMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                DialogRow(label="Файл"){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .dashedBorder(color = Color.LightGray, cornerRadius = 12.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Text("Нажмите, чтобы выбрать файл",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                            style = MaterialTheme.typography.bodySmall)
                    }
                }
                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = {
                        if(fileName.isNotBlank()){
                            onUploadClick(fileName, selectedCategory, selectedFolder)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ){
                    Text("Загрузить", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DialogRow(label: String, content: @Composable () -> Unit){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.weight(0.3f)
        )
        Box(
            modifier = Modifier.weight(0.7f),
            contentAlignment = Alignment.Center
        ){
            content()
        }
    }
}