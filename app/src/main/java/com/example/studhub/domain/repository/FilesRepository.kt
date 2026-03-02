package com.example.studhub.domain.repository

import com.example.studhub.domain.models.FileFolderItem
import kotlinx.coroutines.flow.Flow

interface FilesRepository{
    fun getFolders(): Flow<List<FileFolderItem>>
    suspend fun addFolder(name: String, semester: Int, newId: Int)
    suspend fun deleteFolder(folderId: Int)
}