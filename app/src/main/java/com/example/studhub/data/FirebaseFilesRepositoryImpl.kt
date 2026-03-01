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
}