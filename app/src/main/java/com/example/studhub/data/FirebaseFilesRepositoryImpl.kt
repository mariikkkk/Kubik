package com.example.studhub.data


import com.example.studhub.domain.models.FileFolderItem
import com.example.studhub.domain.repository.FilesRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseFilesRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
): FilesRepository {
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
}