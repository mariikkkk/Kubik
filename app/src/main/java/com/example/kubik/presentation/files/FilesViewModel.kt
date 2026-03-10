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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FilesViewModel: ViewModel() {

    private val repository: FilesRepository = FirebaseFilesRepositoryImpl()

    private val _isUploading = MutableStateFlow(false)                                       // StateFlow хранит в себе последнее значение (умеет сообщать об изменениях)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()                                // Переменная для публичного просмотра. Записать ничего нельзя

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

    fun addFile(context: Context, fileName: String, category: FileCategory, folderId: Int, fileUri: Uri) {
        viewModelScope.launch {
            _isUploading.value = true                                                                               // Включение индикатор загрузки
            try{
                val newId = (1..Int.MAX_VALUE).random()
                val extension = fileName.substringAfterLast('.').lowercase()
                val type = when(extension) {
                    "pdf" -> FileType.PDF
                    "doc", "docx" -> FileType.DOCX
                    "txt" -> FileType.TXT
                    "ppt", "pptx" -> FileType.PPTX
                    "zip", "rar" -> FileType.ZIP
                    "jpg", "jpeg" -> FileType.JPEG
                    else -> FileType.PDF
                }
                val newFile = FileItem(
                    newId,
                    folderId,
                    fileName,
                    "? MB",
                    type,
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    "Студент",
                    category,
                    fileUrl = ""
                )
                repository.addFile(context, newFile, fileUri)
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

    fun downloadFile(context: Context, url: String, fileName: String){
        if (url.isBlank()) return
        try{
            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle(fileName)                                                                                         // Имя файла в шторке
                .setDescription("Скачивание файла из StudHub")                                                              // описание в шторке
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)                     // уведомление остаенется после загрузки
                .setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_DOWNLOADS, fileName) // указываем куда сохранять
                .setAllowedOverMetered(true)                                                                                // разрешение на скачку через мобильный интернет

            val extension = fileName.substringAfterLast(".", "")
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            if (mimeType != null){
                request.setMimeType(mimeType)
            }else{
                request.setMimeType("*/*")
            }

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun shareFile(context: Context, url: String, fileName: String){
        val lastPartUrlIndex = url.indexOfLast{it == '/'}
        val lastPartUrl = url.substring(lastPartUrlIndex + 1)
        val basePartUrl = url.take(lastPartUrlIndex + 1)
        val encodedLastUrl = Uri.encode(lastPartUrl).toString()
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Файл из StudHub")
        intent.putExtra(Intent.EXTRA_TEXT, "Держи файл «$fileName» из StudHub\nСкачать можно по ссылке: ${basePartUrl + encodedLastUrl}")
        val chooser = Intent.createChooser(intent, "Поделиться файлом")
        context.startActivity(chooser)

    }
}

