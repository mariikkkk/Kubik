package com.example.kubik.presentation.files

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.kubik.R
import com.example.kubik.domain.models.FileCategory
import com.example.kubik.domain.models.FileFolderItem
import com.example.kubik.presentation.components.SearchBar
import com.example.kubik.presentation.files.components.CustomDeleteDialog
import com.example.kubik.presentation.files.components.CustomFolderDialog
import com.example.kubik.presentation.files.components.CustomUploadDialog
import com.example.kubik.presentation.theme.KubikTheme

@Composable
fun FilesListScreen(
    filesFolders: List<FileFolderItem>,
    innerPadding: PaddingValues,
    onFolderClick: (Int) -> Unit,
    searchQuery: String,
    selectedSemester: Int,
    isUploading: Boolean = false,
    onSemesterChange: (Int) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onAddFileClick: (String, FileCategory, Int, Uri ) -> Unit,
    onAddFolderClick: (String) -> Unit,
    onDeleteFolderSwipe: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // Состояние для открытия/закрытия списка семестров
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var isFabExpanded by remember { mutableStateOf(false) }
    var showAddFolderDialog by remember { mutableStateOf(false )}
    var folderIdToDelete by remember { mutableStateOf<Int?>(null) }

    var wasUploading by remember { mutableStateOf(false) }
    LaunchedEffect(isUploading) {
        if(isUploading){
            wasUploading = true
        }
        if(!isUploading && wasUploading){
            wasUploading = false
            showAddDialog = false
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = innerPadding.calculateTopPadding() + 8.dp,
                start = 16.dp,
                end = 16.dp
            )
        ,
    ) {
        Column(
            modifier =
                Modifier.fillMaxSize()
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
                            .clickable { expanded = true },

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
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        Modifier.heightIn(max = 250.dp)
                    ){
                        for (semester in 1..12){
                            DropdownMenuItem(
                                text = { Text("$semester семестр") },
                                onClick = {
                                    onSemesterChange(semester)
                                    expanded = false
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
            SearchBar(
                searchQuery,
                onSearchQueryChange,
                "Поиск по названию...",
                Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filesFolders, key = { it.id }) { item ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            positionalThreshold = { totalDistance -> totalDistance * 0.3f},
                            confirmValueChange = { dismissValue ->
                                when(dismissValue){
                                    SwipeToDismissBoxValue.EndToStart -> {
                                        folderIdToDelete = item.id
                                        false
                                    }
                                    SwipeToDismissBoxValue.Settled -> {
                                        false
                                    }
                                    SwipeToDismissBoxValue.StartToEnd -> {
                                        false
                                    }
                                }
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                val color by animateColorAsState(
                                    targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart)
                                        MaterialTheme.colorScheme.errorContainer
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(color)
                                        .padding(end = 20.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ){
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Удалить папку",
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            },
                            content = {
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
                        )
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        ) {
            AnimatedVisibility(isFabExpanded) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            showAddFolderDialog = true
                            isFabExpanded = false
                                  },
                        icon = {
                            Icon(painter = painterResource(R.drawable.folderadd),
                                contentDescription = "Создать папку")
                        },
                        text = { Text("Создать папку") },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ExtendedFloatingActionButton(
                        onClick = {
                            showAddDialog = true
                            isFabExpanded = false
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.download),
                                contentDescription = "Загрузить файл")

                        },
                        text = { Text("Загрузить файл") },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
            FloatingActionButton(
                onClick = { isFabExpanded = !isFabExpanded }
            ){
                Icon(
                    imageVector = if (isFabExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.Add,
                    contentDescription = "Действия"

                )
            }
        }
        if (showAddDialog){
            val allFolders = filesFolders.map {it.name}
            val defaultFolder = allFolders.firstOrNull() ?: ""
            CustomUploadDialog(
                filesFolders,
                filesFolders.firstOrNull()!!.id,
                isUploading = isUploading,
                onDismiss = { showAddDialog = false },
                onUploadClick = { fileName, category, folderId, uri ->
                    onAddFileClick(fileName, category, folderId, uri)
                    //showAddDialog = false
                }
            )
        }
        if(showAddFolderDialog){
            CustomFolderDialog(
                onDismiss = { showAddFolderDialog = false },
                onCreateClick = { folderName ->
                    onAddFolderClick(folderName)
                    showAddFolderDialog = false
                }
            )
        }
        if (folderIdToDelete != null){
            CustomDeleteDialog(
                onDismiss = { folderIdToDelete = null },
                onConfirm = {
                    onDeleteFolderSwipe(folderIdToDelete!!)
                    folderIdToDelete = null
                }
            )
        }
    }
}

@PreviewLightDark
@Composable
fun GreetingPreview() {
    KubikTheme {
        FilesListScreen(
            listOf(FileFolderItem(1,"Математический анализ", 12, 1),
            FileFolderItem(2,"Дискретная математика", 12, 2),
            FileFolderItem(3,"Линейная алгебра", 16, 12),
            FileFolderItem(4,"ООП", 32, 3),),
            PaddingValues(0.dp),
            onFolderClick = {},
            searchQuery = "",
            selectedSemester = 1,
            onSemesterChange = {},
            onSearchQueryChange = {},
            onAddFileClick = { s,d,f, u ->},
            onAddFolderClick =  {s ->},
            onDeleteFolderSwipe =  {i ->})
    }
}