package com.example.kubik.domain.usecase

import com.example.kubik.domain.repository.FilesRepository
import javax.inject.Inject

class DeleteFileUseCase @Inject constructor(
    private val filesRepository: FilesRepository
)
{
    suspend operator fun invoke(fileId: Int, folderId: Int) {
        filesRepository.deleteFile(fileId, folderId)
    }
}