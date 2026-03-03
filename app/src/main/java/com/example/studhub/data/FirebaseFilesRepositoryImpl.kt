package com.example.studhub.data


import android.net.Uri
import com.example.studhub.domain.models.FileFolderItem
import com.example.studhub.domain.models.FileItem
import com.example.studhub.domain.repository.FilesRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseFilesRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
): FilesRepository {
    private val storage = FirebaseStorage.getInstance()

    override fun getFolders(): Flow<List<FileFolderItem>> = callbackFlow{
        val collection = firestore.collection("folders")
        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null){
                val folders = snapshot.documents.mapNotNull { it.toFileFolderItem() }
                trySend(folders)

            }

        }
        awaitClose {
            listener.remove()

        }
    }

    override suspend fun addFolder(name: String, semester: Int, newId: Int) {
        val newFolderMap = hashMapOf(
            "id" to newId,
            "name" to name,
            "countFiles" to 0,
            "semester" to semester
        )
        firestore.collection("folders").add(newFolderMap)
    }

    override suspend fun deleteFolder(folderId: Int) {
        firestore.collection("folders").whereEqualTo("id", folderId).get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    document.reference.delete()
                }
            }

    }

    override fun getFiles(folderId: Int): Flow<List<FileItem>> = callbackFlow{
        val collection = firestore.collection("files")
        val query = collection.whereEqualTo("folderId", folderId)
        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null){
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null){
                val files = snapshot.documents.mapNotNull { it.toFileItem() }
                trySend(files)

            }
        }
        awaitClose {
            listener.remove()
        }
    }



    override suspend fun addFile(file: FileItem, fileUri: Uri) {
        try {
            val fileNameInStorage = "${System.currentTimeMillis()}_${file.name}"
            val storageRef = storage.reference.child("files/${file.folderId}/$fileNameInStorage")
            storageRef.putFile(fileUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            val newFileMap = hashMapOf(
                "id" to file.id,
                "folderId" to file.folderId,
                "name" to file.name,
                "size" to file.size,
                "type" to file.type.name,
                "date" to file.date,
                "author" to file.author,
                "category" to file.category.name,
                "fileUri" to downloadUrl
            )
            firestore.collection("files").add(newFileMap)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun deleteFile(fileId: Int) {
        firestore.collection("files").whereEqualTo("id", fileId).get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    document.reference.delete()
                }
            }

    }
}