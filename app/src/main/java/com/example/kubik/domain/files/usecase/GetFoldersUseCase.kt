package com.example.kubik.domain.files.usecase

import com.example.kubik.domain.files.models.FileFolderItem
import com.example.kubik.domain.files.repository.FilesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoldersUseCase @Inject constructor(
    private val filesRepository: FilesRepository
) {
    operator fun invoke(groupId: String): Flow<List<FileFolderItem>> = filesRepository.getFolders(groupId)
}