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
import com.example.kubik.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
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
    private val addFolderUseCase: AddFolderUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    val currentUser = getUserUseCase().stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private val _isUploading = MutableStateFlow(false)                                       // StateFlow хранит в себе последнее значение (умеет сообщать об изменениях)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()                                // Переменная для публичного просмотра. Записать ничего нельзя

    private var _firebaseFolders by mutableStateOf<List<FileFolderItem>>(emptyList())

    private val _firebaseFiles = MutableStateFlow<List<FileItem>>(emptyList())
    val firebaseFiles: StateFlow<List<FileItem>> = _firebaseFiles.asStateFlow()

    private var fileJob: Job? = null
    private var folderJob: Job? = null
    init{
        viewModelScope.launch{
            currentUser.collect{ user ->
                if(user != null && user.groupId != null){
                    loadFolders(user.groupId)
                }
            }
        }
    }

    private fun loadFolders(groupId: String){
        folderJob?.cancel()
        folderJob = viewModelScope.launch {
            getFoldersUseCase(groupId).collect { foldersFromDb ->
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
        val user = currentUser.value ?: return
        viewModelScope.launch {
            _isUploading.value = true                                                                               // Включение индикатор загрузки
            try{
                addFileUseCase( fileName = fileName, category = category, folderId = folderId, fileUri = fileUri, currentUser = user)
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

    fun deleteFile(fileId: Int, folderId: Int){
        viewModelScope.launch {
            deleteFileUseCase(fileId, folderId)
        }
    }

    fun addFolder(folderName: String) {
        val user = currentUser.value ?: return
        val groupId = user.groupId ?: ""
        viewModelScope.launch {
            addFolderUseCase(folderName, selectedSemester, groupId)
        }
    }
    fun deleteFolder(folderId: Int){
        viewModelScope.launch {
            deleteFolderUseCase(folderId)
        }
    }
}

