package com.example.studhub.data

import com.example.studhub.domain.models.FileCategory
import com.example.studhub.domain.models.FileFolderItem
import com.example.studhub.domain.models.FileItem
import com.example.studhub.domain.models.FileType
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toFileFolderItem(): FileFolderItem?{
    return try{
        val id = getLong("id")?.toInt() ?: return null
        val name = getString("name") ?: return null
        val countFiles = getLong("countFiles")?.toInt() ?: 0
        val semester = getLong("semester")?.toInt() ?: 1
        FileFolderItem(id, name, countFiles, semester)

    } catch (e: Exception){
        null
    }
}

fun DocumentSnapshot.toFileItem(): FileItem?{
    return try{
        val id = getLong("id")?.toInt() ?: return null
        val folderId = getLong("folderId")?.toInt() ?: return null
        val name = getString("name") ?: return null
        val size = getString("size") ?: "0 MB"
        val date = getString("date") ?: ""
        val author = getString("author") ?: ""
        val typeString = getString("type") ?: FileType.PDF.name
        val type = runCatching { FileType.valueOf(typeString) }.getOrDefault(FileType.PDF)
        val categoryString = getString("category") ?: FileCategory.OTHER.name
        val category = runCatching { FileCategory.valueOf(categoryString) }.getOrDefault(FileCategory.OTHER)
        val fileUrl = getString("fileUrl") ?: ""

        FileItem(
            id,
            folderId,
            name,
            size,
            type,
            date,
            author,
            category,
            fileUrl
        )
    } catch (e: Exception){
        null
    }
}