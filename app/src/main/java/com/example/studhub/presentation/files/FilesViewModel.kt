package com.example.studhub.presentation.files

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studhub.data.FirebaseFilesRepositoryImpl
import com.example.studhub.domain.models.FileCategory
import com.example.studhub.domain.models.FileFolderItem
import com.example.studhub.domain.models.FileItem
import com.example.studhub.domain.models.FileType
import com.example.studhub.domain.repository.FilesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FilesViewModel: ViewModel() {

    private val repository: FilesRepository = FirebaseFilesRepositoryImpl()
    private var _firebaseFolders by mutableStateOf<List<FileFolderItem>>(emptyList())
    init{
        viewModelScope.launch{
            repository.getFolders().collect { foldersFromDb ->
                _firebaseFolders = foldersFromDb
            }
        }
    }

    private val _firebaseFiles = MutableStateFlow<List<FileItem>>(emptyList())
    val firebaseFiles: StateFlow<List<FileItem>> = _firebaseFiles.asStateFlow()

    private var fileJob: Job? = null

    fun loadFilesForFolder(folderId: Int){
        fileJob?.cancel()
        fileJob = viewModelScope.launch {
            repository.getFiles(folderId).collect { filesFromDb ->
                _firebaseFiles.value = filesFromDb
            }
        }
    }

    fun addFile(fileName: String, category: FileCategory, folderId: Int, fileUri: Uri) {
        viewModelScope.launch {
            val newId = (_firebaseFiles.value.maxOfOrNull { it.id } ?: 0) + 1
            val newFile = FileItem(
                newId,
                folderId,
                fileName,
                "? MB",
                FileType.PDF,
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                "Студент",
                category,
                fileUrl = ""
            )
            repository.addFile(newFile, fileUri)
        }
    }

    var searchQuery by mutableStateOf("")
    private set

    var selectedSemester by mutableStateOf(1)  // П
    private set

    val folderList: List<FileFolderItem>
        get() = _firebaseFolders.filter { folder ->
            folder.semester == selectedSemester &&
                    folder.name.contains(searchQuery, ignoreCase = true)
        }
    fun updateSearchQuery(query: String){
        searchQuery = query
    }

    fun updateSelectedSemester(semester: Int){
        selectedSemester = semester
    }

    fun deleteFile(fileId: Int){
        viewModelScope.launch {
            repository.deleteFile(fileId)
        }
    }

    fun addFolder(folderName: String) {
        val newId = (_firebaseFolders.maxOfOrNull { it.id } ?: 0) + 1
        viewModelScope.launch {
            repository.addFolder(folderName, selectedSemester, newId)
        }
    }
    fun deleteFolder(folderId: Int){
        viewModelScope.launch {
            repository.deleteFolder(folderId)
        }
    }
}

