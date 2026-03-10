package com.example.kubik.domain.repository

import android.content.Context
import android.net.Uri
import com.example.kubik.domain.models.FileFolderItem
import com.example.kubik.domain.models.FileItem
import kotlinx.coroutines.flow.Flow

interface FilesRepository{
    fun getFolders(): Flow<List<FileFolderItem>>
    suspend fun addFolder(name: String, semester: Int, newId: Int)
    suspend fun deleteFolder(folderId: Int)

    fun getFiles(folderId: Int): Flow<List<FileItem>>
    suspend fun addFile(context: Context, file: FileItem, fileUri: Uri)
    suspend fun deleteFile(fileId: Int)
}