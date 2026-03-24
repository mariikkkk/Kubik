package com.example.kubik.domain.usecase

import com.example.kubik.domain.models.FileItem
import com.example.kubik.domain.repository.FilesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilesUseCase @Inject constructor(
    private val filesRepository: FilesRepository
) {
    operator fun invoke(folderId: Int): Flow<List<FileItem>> = filesRepository.getFiles(folderId = folderId)


}