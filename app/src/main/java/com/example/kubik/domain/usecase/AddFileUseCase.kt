package com.example.kubik.domain.usecase

import android.net.Uri
import com.example.kubik.domain.models.FileCategory
import com.example.kubik.domain.models.FileItem
import com.example.kubik.domain.models.FileType
import com.example.kubik.domain.repository.FilesRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AddFileUseCase @Inject constructor(
    private val filesRepository: FilesRepository
) {
    suspend operator fun invoke(
        fileName: String,
        category: FileCategory,
        folderId: Int,
        fileUri: Uri
    ){
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
        filesRepository.addFile(newFile, fileUri)
    }
}