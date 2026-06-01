package com.example.kubik.domain.files.usecase

import com.example.kubik.domain.files.repository.FilesRepository
import javax.inject.Inject

class RenameFileUseCase @Inject constructor(
    private val filesRepository: FilesRepository
) {
    suspend operator fun invoke(fileId: Int, newName: String){
        filesRepository.renameFile(fileId, newName)
    }
}