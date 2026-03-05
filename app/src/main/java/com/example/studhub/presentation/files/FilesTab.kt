package com.example.studhub.presentation.files

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FilesTab(viewModel: FilesViewModel = viewModel()){
    var selectedFolder by remember { mutableStateOf<Int?>(null) }
    val context = LocalContext.current
    val isUploading by viewModel.isUploading.collectAsState()                                        // collectAsState подписывает на поток (кран, через который течет поток данных)

    if (selectedFolder == null){
        FilesListScreen(
            viewModel.folderList,
            onFolderClick = { selectedFolderId ->
                    selectedFolder = selectedFolderId
                    viewModel.loadFilesForFolder(selectedFolderId)
            },
            searchQuery = viewModel.searchQuery,
            selectedSemester = viewModel.selectedSemester,
            onSemesterChange = {semester -> viewModel.updateSelectedSemester(semester)},
            onSearchQueryChange = {query -> viewModel.updateSearchQuery(query)},
            onAddFileClick = {fileName, category, targetFolderId, fileUri ->
                viewModel.addFile(context, fileName, category, folderId = targetFolderId, fileUri)
            },
            isUploading = isUploading,
            onAddFolderClick = {folderName ->
                viewModel.addFolder(folderName)
            },
            onDeleteFolderSwipe = { folderId ->
                viewModel.deleteFolder(folderId)
            }
        )
    }
    else{
        val folderName = viewModel.folderList.find { it.id == selectedFolder }?.name ?: "Ошибка"
        val filesForThisFolder by viewModel.firebaseFiles.collectAsState()
        FileDetailsScreen(
            selectedFolder!!,
            folderName,
            filesForThisFolder,
            isUploading = isUploading,
                { selectedFolder = null},
            {fileName, category, fileUri ->
                    viewModel.addFile(context,
                        fileName, category, selectedFolder!!, fileUri)
            },
            {fileId -> viewModel.deleteFile(fileId)},
        onDownloadFileClick = {fileUrl, fileName ->
            viewModel.downloadFile(context, fileUrl, fileName)
        }
        )

    }

}