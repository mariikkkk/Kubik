package com.example.kubik.presentation.files

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubik.data.FirebaseFilesRepositoryImpl
import com.example.kubik.domain.models.FileCategory
import com.example.kubik.domain.models.FileFolderItem
import com.example.kubik.domain.models.FileItem
import com.example.kubik.domain.models.FileType
import com.example.kubik.domain.repository.FilesRepository
import com.example.kubik.domain.usecase.AddFileUseCase
import com.example.kubik.domain.usecase.AddFolderUseCase
import com.example.kubik.domain.usecase.DeleteFileUseCase
import com.example.kubik.domain.usecase.DeleteFolderUseCase
import com.example.kubik.domain.usecase.GetFilesUseCase
import com.example.kubik.domain.usecase.GetFoldersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    private val getFoldersUseCase: GetFoldersUseCase,
    private val getFilesUseCase: GetFilesUseCase,
    private val addFileUseCase: AddFileUseCase,
    private val deleteFileUseCase: DeleteFileUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val addFolderUseCase: AddFolderUseCase
) : ViewModel() {


    private val _isUploading = MutableStateFlow(false)                                       // StateFlow хранит в себе последнее значение (умеет сообщать об изменениях)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()                                // Переменная для публичного просмотра. Записать ничего нельзя

    private var _firebaseFolders by mutableStateOf<List<FileFolderItem>>(emptyList())
    private val _firebaseFiles = MutableStateFlow<List<FileItem>>(emptyList())
    val firebaseFiles: StateFlow<List<FileItem>> = _firebaseFiles.asStateFlow()

    private var fileJob: Job? = null
    init{
        viewModelScope.launch{
            getFoldersUseCase().collect { foldersFromDb ->
                _firebaseFolders = foldersFromDb
            }
        }
    }

    fun loadFilesForFolder(folderId: Int){
        fileJob?.cancel()
        fileJob = viewModelScope.launch {
            getFilesUseCase(folderId).collect { filesFromDb ->
                _firebaseFiles.value = filesFromDb
            }
        }
    }

    fun addFile(fileName: String, category: FileCategory, folderId: Int, fileUri: Uri) {
        viewModelScope.launch {
            _isUploading.value = true                                                                               // Включение индикатор загрузки
            try{
                addFileUseCase( fileName = fileName, category = category, folderId = folderId, fileUri)
            } catch (e: Exception){
                e.printStackTrace()
            } finally {
                _isUploading.value = false                                                                          // Загрузили - отключили
            }

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
            deleteFileUseCase(fileId)
        }
    }

    fun addFolder(folderName: String) {
        val newId = (_firebaseFolders.maxOfOrNull { it.id } ?: 0) + 1
        viewModelScope.launch {
            addFolderUseCase(folderName, selectedSemester, newId)
        }
    }
    fun deleteFolder(folderId: Int){
        viewModelScope.launch {
            deleteFolderUseCase(folderId)
        }
    }
}

