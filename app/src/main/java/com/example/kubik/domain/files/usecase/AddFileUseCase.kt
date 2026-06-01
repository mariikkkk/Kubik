package com.example.kubik.domain.files.usecase

import android.net.Uri
import com.example.kubik.domain.files.models.FileCategory
import com.example.kubik.domain.files.models.FileItem
import com.example.kubik.domain.files.models.FileType
import com.example.kubik.domain.models.User
import com.example.kubik.domain.files.repository.FilesRepository
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
        fileUri: Uri,
        currentUser: User
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
        val fullName = "${currentUser.firstName} ${currentUser.lastName}"
        val newFile = FileItem(
            newId,
            folderId,
            fileName,
            0L,
            type,
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
            fullName,
            category,
            fileUrl = ""
        )
        filesRepository.addFile(newFile, fileUri)
    }
}