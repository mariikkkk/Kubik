package com.example.kubik.domain.files.usecase

import com.example.kubik.domain.files.repository.FilesRepository
import javax.inject.Inject

class DeleteFolderUseCase @Inject constructor(
    private val filesRepository: FilesRepository
) {
    suspend operator fun invoke(folderId: Int){
        filesRepository.deleteFolder(folderId)
    }
}