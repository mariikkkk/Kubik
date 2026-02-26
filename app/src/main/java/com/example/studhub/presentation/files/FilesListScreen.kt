package com.example.studhub.presentation.files

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studhub.R
import com.example.studhub.domain.models.FileCategory
import com.example.studhub.domain.models.FileFolderItem
import com.example.studhub.presentation.files.components.CustomUploadDialog
import com.example.studhub.presentation.theme.StudHubTheme

@Composable
fun FilesListScreen(
    filesFolders: List<FileFolderItem>,
    onFolderClick: (Int) -> Unit,
    searchQuery: String,
    selectedSemester: Int,
    onSemesterChange: (Int) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onAddFileClick: (String, FileCategory, String) -> Unit
) {
    var expaneded by remember { mutableStateOf(false) } // Состояние для открытия/закрытия списка семестров
    var showAddDialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier =
                Modifier.fillMaxSize()
                    .padding(horizontal = 16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Предметы",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Box() { // Фильтрация по семестрам
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .clickable { expaneded = true },

                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Text(
                            text = "$selectedSemester семестр",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            //modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp)

                        )
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = "Открыть",
                            //modifier = Modifier.padding(end = 8.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = expaneded,
                        onDismissRequest = { expaneded = false },
                        Modifier.heightIn(max = 250.dp)
                    ){
                        for (semester in 1..12){
                            DropdownMenuItem(
                                text = { Text("$semester семестр") },
                                onClick = {
                                    onSemesterChange(semester)
                                    expaneded = false
                                }
                            )
                        }

                    }
                }
//            Spacer(modifier = Modifier.weight(1f))
//            Button(
//                onClick = {},
//                contentPadding = PaddingValues(
//                    start = 12.dp,
//                    end = 12.dp,
//                    top = 4.dp,
//                    bottom = 4.dp
//                )
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.download),
//                    contentDescription = "Загрузить"
//                )
//                Spacer(Modifier.width(4.dp))
//                Text("Загрузить")
//            }
            }
            Spacer(modifier = Modifier.height(10.dp))
            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.5f))
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Поиск",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Box(modifier = Modifier.weight(1f)){
                            if(searchQuery.isEmpty()){
                                Text("Поиск файлов...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha=0.5f)
                                )
                            }
                            innerTextField()
                        }
                    }
                }

            )

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filesFolders, key = { it.id }) { item ->

                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onFolderClick(item.id) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),

                        ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.folder_rec),
                                contentDescription = "folder",
                                modifier = Modifier.size(52.dp)

                            )
                            Spacer(Modifier.width(10.dp))
                            Column() {
                                Text(
                                    item.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    "${item.countFiles} файлов",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            Spacer(Modifier.weight(1f))
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Открыть")


                        }

                    }
                }

            }
        }
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 16.dp)
        ){
            Icon(painter = painterResource(R.drawable.download),
                contentDescription = "Добавить")
        }
        if (showAddDialog){
            val allFolders = filesFolders.map {it.name}
            val defaultFolder = allFolders.firstOrNull() ?: ""
            CustomUploadDialog(
                defaultFolder,
                allFolders,
                onDismiss = { showAddDialog = false },
                onUploadClick = { fileName, category, targetFolder ->
                    onAddFileClick(fileName, category, targetFolder)
                    showAddDialog = false
                }
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StudHubTheme {
        FilesListScreen(listOf(FileFolderItem(1,"Математический анализ", 12, 1),
            FileFolderItem(2,"Дискретная математика", 12, 2),
            FileFolderItem(3,"Линейная алгебра", 16, 12),
            FileFolderItem(4,"ООП", 32, 3),), onFolderClick = {}, searchQuery = "", selectedSemester = 1, onSemesterChange = {}, onSearchQueryChange = {}, onAddFileClick = { s,d,f ->})
    }
}