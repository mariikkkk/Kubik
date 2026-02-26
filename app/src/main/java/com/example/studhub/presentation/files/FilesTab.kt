package com.example.studhub.presentation.files

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studhub.domain.models.FileCategory
import com.example.studhub.domain.models.FileItem
import com.example.studhub.domain.models.FileType

@Composable
fun FilesTab(viewModel: FilesViewModel = viewModel()){
    var selectedFolder by remember { mutableStateOf<Int?>(null) }
    if (selectedFolder == null){
        FilesListScreen(viewModel.folderList,
            onFolderClick = { selectedFolderId ->
                    selectedFolder = selectedFolderId
            },
            searchQuery = viewModel.searchQuery,
            selectedSemester = viewModel.selectedSemester,
            onSemesterChange = {semester -> viewModel.updateSelectedSemester(semester)},
            onSearchQueryChange = {query -> viewModel.updateSearchQuery(query)},
            onAddFileClick = {fileName, category, targetFolder ->
                viewModel.addFileByFolderName(targetFolder, fileName, category)
            })
    }
    else{
        val folderName = viewModel.folderList.find { it.id == selectedFolder }?.name ?: "Ошибка"

        val filesForThisFolder = viewModel.folderFiles.filter { it.folderId == selectedFolder }
        FileDetailsScreen(folderName,
            filesForThisFolder,
                { selectedFolder = null}, {fileName, category ->
                    viewModel.addFile(selectedFolder!!, fileName, category)
            },
            {fileId -> viewModel.deleteFile(fileId)})
    }

}