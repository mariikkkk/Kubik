package com.example.studhub.presentation.files

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import kotlinx.coroutines.launch

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


    val folderFiles = mutableStateListOf(
        FileItem(1, 1,"Лекция №1", "2.5 MB", FileType.PDF, "01.02.2024", "Иванов И.И.", FileCategory.LECTURES),
        FileItem(2, 3,"Лекция №2", "1.5 MB", FileType.PDF, "02.02.2024", "Петров П.П.", FileCategory.LECTURES),
        FileItem(3, 1,"ТРПП Практика №1", "1000 MB", FileType.DOCX, "03.12.2026", "Куликов А", FileCategory.PRACTICE),
        FileItem(4, 1,"Скриншот ошибки", "1000 MB", FileType.JPEG, "03.12.2026", "Куликов А", FileCategory.OTHER),
        FileItem(5, 4,"ООП Лекция №1", "1000 MB", FileType.PPTX, "03.12.2026", "Куликов А", FileCategory.LECTURES)

    )

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

    fun addFile(folderId: Int, fileName: String, category: FileCategory){
        val newId = (folderFiles.maxOfOrNull { it.id } ?: 0) + 1

        val newFile = FileItem(
            newId,
            folderId,
            fileName,
            "6.7 MB",
            FileType.PDF,
            "26.02.2026",
            "Котельников",
            category
        )

        folderFiles.add(newFile)
    }

    fun deleteFile(fileId: Int){
        folderFiles.removeIf { it.id == fileId }
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

