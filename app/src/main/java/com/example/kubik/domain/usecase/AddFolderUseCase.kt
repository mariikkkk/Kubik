package com.example.kubik.domain.usecase

import com.example.kubik.domain.repository.FilesRepository
import javax.inject.Inject

class AddFolderUseCase @Inject constructor(
    private val filesRepository: FilesRepository
){
    suspend operator fun invoke(folderName: String, selectedSemester: Int, newId: Int){
        filesRepository.addFolder(folderName, selectedSemester, newId)
    }

}