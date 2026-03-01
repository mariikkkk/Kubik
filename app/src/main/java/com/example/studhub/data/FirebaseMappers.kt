package com.example.studhub.data

import com.example.studhub.domain.models.FileFolderItem
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