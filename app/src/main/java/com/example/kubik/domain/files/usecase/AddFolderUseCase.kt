package com.example.kubik.domain.files.usecase

import com.example.kubik.domain.files.repository.FilesRepository
import javax.inject.Inject

class AddFolderUseCase @Inject constructor(
    private val filesRepository: FilesRepository
){
    suspend operator fun invoke(folderName: String, selectedSemester: Int, groupId: String){
        filesRepository.addFolder(folderName, selectedSemester, groupId)
    }
}