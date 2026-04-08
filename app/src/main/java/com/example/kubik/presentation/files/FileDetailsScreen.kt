package com.example.kubik.presentation.files

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.kubik.R
import com.example.kubik.domain.models.FileCategory
import com.example.kubik.domain.models.FileFolderItem
import com.example.kubik.domain.models.FileItem
import com.example.kubik.presentation.files.components.CustomUploadDialog
import com.example.kubik.presentation.files.components.FileCard
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileDetailsScreen(
    folderId: Int,
    folderName: String,
    files: List<FileItem>,
    isUploading: Boolean = false,
    innerPadding: PaddingValues,
    onBackClick: () -> Unit,
    onAddFileClick: (String, FileCategory, Uri) -> Unit,
    onDeleteFileClick: (Int) -> Unit,
    onDownloadFileClick: (String, String) -> Unit,
    onRenameFileClick: (Int, String) -> Unit,
    onShareFileClick: (String, String) -> Unit
){
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(FileCategory.ALL) }

    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var newFileName by remember { mutableStateOf("") }


    val filteredFiles = files.filter { file ->
        val matchesSearch = file.name.contains(searchQuery, ignoreCase = true)
        val matchesVategory = selectedCategory == FileCategory.ALL || file.category == selectedCategory
        matchesSearch && matchesVategory
    }

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

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(
            top = innerPadding.calculateTopPadding() + 8.dp,
            start = 12.dp,
            end = 12.dp
        )
    ){
        Column()
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.offset(x = (-12).dp)
                )
                {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Вернуться назад к списку папок",
                    )
                }
                Text(
                    text = folderName,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).offset(x = (-12).dp),
                    fontWeight = FontWeight.Bold
                )

            }
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
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
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Поиск",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    "Поиск файлов...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                            innerTextField()
                        }
                    }
                }

            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                FileCategory.entries.forEach { categoryEnum ->
                    val isSelected = selectedCategory == categoryEnum

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                            .clickable { selectedCategory = categoryEnum }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center

                    )
                    {
                        Text(
                            categoryEnum.title,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                    Spacer(Modifier.width(8.dp))

                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 10.dp)
            ) {
                items(items = filteredFiles, key = { it.id }) { file ->
                    FileCard(
                        file = file,
                        onDownloadClick = { onDownloadFileClick(file.fileUrl, file.name) },
                        onShareClick = {
                            onShareFileClick(file.fileUrl, file.name)
                        },
                        onDeleteClick = {
                            onDeleteFileClick(file.id)
                        },
                        onRenameClick = {},
                        modifier = Modifier.animateItem()
                    )
                    Spacer(Modifier.height(4.dp))
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
        if(showAddDialog){
            CustomUploadDialog(
                listOf(FileFolderItem(folderId, folderName, 0, 0)),
                folderId,
                isUploading = isUploading,
                onDismiss = {showAddDialog = false},
                onUploadClick = {
                name, category, folder, uri ->
                    onAddFileClick(name, category, uri)
                        //showAddDialog = false
            })
       }
    }
}






