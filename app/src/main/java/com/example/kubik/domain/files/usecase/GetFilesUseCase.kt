package com.example.kubik.domain.files.usecase

import com.example.kubik.domain.files.models.FileItem
import com.example.kubik.domain.files.repository.FilesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilesUseCase @Inject constructor(
    private val filesRepository: FilesRepository
) {
    operator fun invoke(folderId: Int): Flow<List<FileItem>> = filesRepository.getFiles(folderId = folderId)


}