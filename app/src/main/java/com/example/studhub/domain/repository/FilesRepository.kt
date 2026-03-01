package com.example.studhub.domain.repository

import com.example.studhub.domain.models.FileFolderItem
import kotlinx.coroutines.flow.Flow

interface FilesRepository{
    fun getFolders(): Flow<List<FileFolderItem>>
}