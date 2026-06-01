package com.example.kubik.data

import androidx.compose.runtime.snapshotFlow
import com.example.kubik.domain.announcement.model.AnnouncementItem
import com.example.kubik.domain.announcement.repository.AnnouncementRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AnnouncementRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : AnnouncementRepository{
    private val announcementCollection get() = firestore.collection("announcements")
    override fun getAnnouncements(groupId: String): Flow<List<AnnouncementItem>> = callbackFlow{
        val listener = announcementCollection
            .whereEqualTo("groupId", groupId)
            .addSnapshotListener { snapshot, error ->
                if(error != null){
                    close(error)
                    return@addSnapshotListener
                }
                if(snapshot != null){
                    val announcement = snapshot.documents.mapNotNull {
                        it.toObject(AnnouncementItem::class.java)
                            ?.copy(id = it.id)
                    }.sortedByDescending { it.createdAt }
                    trySend(announcement)
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun createAnnouncement(announcement: AnnouncementItem): Result<Unit> = runCatching{
        val announcementRef = announcementCollection.document()
        announcementRef.set(announcement.copy(id = announcementRef.id)).await()
    }

    override suspend fun deleteAnnouncement(announcementId: String): Result<Unit> = runCatching{
        announcementCollection
            .document(announcementId)
            .delete()
            .await()
    }

}