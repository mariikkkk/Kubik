package com.example.kubik.domain.usecase

import com.example.kubik.domain.models.FileFolderItem
import com.example.kubik.domain.repository.FilesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoldersUseCase @Inject constructor(
    private val filesRepository: FilesRepository
) {
    operator fun invoke(groupId: String): Flow<List<FileFolderItem>> = filesRepository.getFolders(groupId)

}