package com.example.kubik.domain.files.repository

import android.net.Uri
import com.example.kubik.domain.files.models.FileFolderItem
import com.example.kubik.domain.files.models.FileItem
import kotlinx.coroutines.flow.Flow

interface FilesRepository{
    fun getFolders(groupId: String): Flow<List<FileFolderItem>>
    suspend fun addFolder(name: String, semester: Int, groupId: String)
    suspend fun deleteFolder(folderId: Int)

    fun getFiles(folderId: Int): Flow<List<FileItem>>
    suspend fun addFile(file: FileItem, fileUri: Uri)
    suspend fun deleteFile(fileId: Int, folderId: Int)
    suspend fun renameFile(fileId: Int, newName: String)
}