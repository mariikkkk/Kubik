package com.example.kubik.presentation.files

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FilesTab(
    viewModel: FilesViewModel = hiltViewModel(),
    innerPadding: PaddingValues
){
    var selectedFolder by rememberSaveable { mutableStateOf<Int?>(null) }
    val context = LocalContext.current
    val isUploading by viewModel.isUploading.collectAsState()                                        // collectAsState подписывает на поток (кран, через который течет поток данных)

    BackHandler(enabled = selectedFolder != null) {
        selectedFolder = null
    }
    if (selectedFolder == null){
        FilesListScreen(
            viewModel.folderList,
            innerPadding,
            onFolderClick = { selectedFolderId ->
                    selectedFolder = selectedFolderId
                    viewModel.loadFilesForFolder(selectedFolderId)
            },
            searchQuery = viewModel.searchQuery,
            selectedSemester = viewModel.selectedSemester,
            onSemesterChange = {semester -> viewModel.updateSelectedSemester(semester)},
            onSearchQueryChange = {query -> viewModel.updateSearchQuery(query)},
            onAddFileClick = {fileName, category, targetFolderId, fileUri ->
                viewModel.addFile(fileName, category, folderId = targetFolderId, fileUri)
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
            innerPadding = innerPadding,
                { selectedFolder = null},
            {fileName, category, fileUri ->
                    viewModel.addFile(
                        fileName, category, selectedFolder!!, fileUri)
            },
            {fileId -> viewModel.deleteFile(fileId, selectedFolder!!)},
            onDownloadFileClick = {fileUrl, fileName ->
                downloadFile(context, fileUrl, fileName)
            },
            onRenameFileClick = { fileId, newName ->
                viewModel.renameFile(fileId!!, newName)
            },
            onShareFileClick = { fileUrl, fileName ->
                shareFile(context, fileUrl, fileName)
            }

        )

    }

}