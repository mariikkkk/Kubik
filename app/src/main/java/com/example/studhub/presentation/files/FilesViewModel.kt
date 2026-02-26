package com.example.studhub.presentation.files

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.studhub.domain.models.FileCategory
import com.example.studhub.domain.models.FileFolderItem
import com.example.studhub.domain.models.FileItem
import com.example.studhub.domain.models.FileType

class FilesViewModel: ViewModel() {

    private val _folderList = mutableStateListOf(
        FileFolderItem(1,"Математический анализ", 12, 1),
        FileFolderItem(10,"Математический анализ", 12, 1),
        FileFolderItem(11,"Математический анализ", 12, 1),
        FileFolderItem(12,"Математический анализ", 12, 1),
        FileFolderItem(13,"Математический анализ", 12, 1),
        FileFolderItem(14,"Математический анализ", 12, 1),
        FileFolderItem(15,"Математический анализ", 12, 1),
        FileFolderItem(16,"Математический анализ", 12, 1),
        FileFolderItem(2,"Дискретная математика", 12, 2),
        FileFolderItem(3,"Линейная алгебра", 16, 12),
        FileFolderItem(4,"ООП", 32, 4),


    )

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
        get() = _folderList.filter { folder ->
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

    fun addFileByFolderName(folderName: String, fileName: String, category: FileCategory) {
        val targetFolder = folderList.find { it.name == folderName }
        if (targetFolder != null) {
            addFile(targetFolder.id, fileName, category)
        }
    }

    fun deleteFile(fileId: Int){
        folderFiles.removeIf { it.id == fileId }
    }
}

