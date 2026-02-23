package com.example.studhub.presentation.files

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FilesTab(viewModel: FilesViewModel = viewModel()){
    FilesListScreen(viewModel.folderList,
        onFolderClick = {
                folderId -> println("Folder clicked: $folderId")
        },
        searchQuery = viewModel.searchQuery,
        selectedSemeser = viewModel.selectedSemester,
        onSemesterChange = {semester -> viewModel.updateSelectedSemester(semester)},
        onSearchQueryChange = {query -> viewModel.updateSearchQuery(query)})
}