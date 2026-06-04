package com.example.kubik.data

import com.example.kubik.domain.queues.models.QueueItem
import com.example.kubik.domain.queues.models.SlotItem
import com.example.kubik.domain.queues.models.SlotStatus
import com.example.kubik.domain.queues.repository.QueuesRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QueuesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): QueuesRepository {
    private val queuesCollection get() = firestore.collection("queues")

    override fun getQueues(groupId: String): Flow<List<QueueItem>> = callbackFlow{
        val listener = queuesCollection
            .whereEqualTo("groupId", groupId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val queues = snapshot.documents.mapNotNull {
                        it
                            .toObject(QueueItem::class.java)
                            ?.copy(id = it.id)
                    }
                    trySend(queues)
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    override fun getQueueSlots(queueId: String): Flow<List<SlotItem>> = callbackFlow{
        val listener = queuesCollection
            .document(queueId)
            .collection("slots")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val slots = snapshot.documents
                        .mapNotNull { it.toObject(SlotItem::class.java) }
                    trySend(slots)
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun createQueue(queue: QueueItem): Result<Unit> = runCatching{
        queuesCollection.add(queue).await()
    }

    override suspend fun joinQueueTransaction(
        queueId: String,
        slotNumber: Int,
        userId: String,
        userName: String
    ): Result<Unit> = runCatching{
        val slotRef = queuesCollection
            .document(queueId)
            .collection("slots")
            .document(slotNumber.toString()) //описываю путь

        firestore.runTransaction { transaction -> //запуск атомарного блока. Блок чтения
            val existing = transaction.get(slotRef) //поиск слота очереди в базе

            if(existing.exists()){
                throw Exception("Место уже занято")
            }
            val queueRef = queuesCollection.document(queueId)
            transaction.update(queueRef, "participantIds", FieldValue.arrayUnion(userId)) //добавление userId в очередь
            transaction.update(queueRef, "userSlots.$userId", slotNumber) //добавление userId в слот
            val slot = SlotItem(
                slotNumber = slotNumber,
                userId = userId,
                userName = userName,
                status = SlotStatus.WAITING.value
            )
            transaction.set(slotRef, slot) //запись слота в базу
        }.await()
    }

    override suspend fun updateSlotStatus(
        slotNumber: Int,
        queueId: String,
        newStatus: String
    ): Result<Unit> = runCatching {
        queuesCollection
            .document(queueId)
            .collection("slots")
            .document(slotNumber.toString())
            .update("status", newStatus)
            .await()
    }

    override suspend fun updateQueue(
        queueId: String,
        updates: Map<String, Any?>
    ): Result<Unit> = runCatching {
        queuesCollection
            .document(queueId)
            .update(updates)
            .await()
    }

    override suspend fun leaveQueue(queueId: String, slotNumber: Int): Result<Unit> = runCatching {
        val slot = queuesCollection
            .document(queueId)
            .collection("slots")
            .document(slotNumber.toString())
            .get()
            .await()

        val userId = slot.getString("userId") ?: ""

        val batch = firestore.batch()
        batch.delete(slot.reference)
        batch.update(
            queuesCollection.document(queueId),
            "participantIds",
            FieldValue.arrayRemove(userId)
        )
        batch.update(queuesCollection.document(queueId), "userSlots.$userId", FieldValue.delete())
        batch.commit().await()
    }

    override suspend fun deleteQueue(queueId: String): Result<Unit> = runCatching {
        val slots = queuesCollection
            .document(queueId)
            .collection("slots")
            .get()
            .await()

        val batch = firestore.batch()
        slots.documents.forEach { batch.delete(it.reference) }
        batch.delete(queuesCollection.document(queueId))
        batch.commit().await()
    }

    override suspend fun closeAndMigrateQueue(
        oldQueueId: String,
        newQueue: QueueItem,
        migratedSlots: List<SlotItem>
    ): Result<Unit> = runCatching{
        val batch = firestore.batch()

        batch.update(
            queuesCollection.document(oldQueueId),
            mapOf(
                "status" to "closed",
                "closedAt" to System.currentTimeMillis()
            )
        )

        val newQueueRef = queuesCollection.document()
        val migratedUserSlots = migratedSlots
            .mapIndexed { index, slot ->
                slot.userId to (index + 1)
            }
            .toMap()
        batch.set(
            newQueueRef,
            newQueue.copy(
                id = newQueueRef.id,
                participantIds = migratedSlots.map{ it.userId},
                userSlots = migratedUserSlots
            )
        )
        migratedSlots.forEachIndexed { index, slot ->
            val newSlotNumber = index + 1
            val newSlotRef = newQueueRef
                .collection("slots")
                .document(newSlotNumber.toString())
            batch.set(newSlotRef, slot.copy(slotNumber = newSlotNumber))
        }
        batch.commit().await()
    }

    override suspend fun getFirstWaitingSlot(queueId: String): Result<SlotItem?> = runCatching{
        queuesCollection
            .document(queueId)
            .collection("slots")
            .whereEqualTo("status", SlotStatus.WAITING.value)
            .orderBy("slotNumber")
            .limit(1)
            .get()
            .await()
            .documents
            .firstOrNull()
            ?.toObject(SlotItem::class.java)
    }

    override fun getQueueById(queueId: String): Flow<QueueItem?> = callbackFlow {
        val listener = queuesCollection
            .document(queueId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val queue = snapshot
                    ?.toObject(QueueItem::class.java)
                    ?.copy(id = snapshot.id)
                trySend(queue)
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun getQueueSlotsOnce(queueId: String): List<SlotItem> {
        return queuesCollection
            .document(queueId)
            .collection("slots")
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(SlotItem::class.java) }
    }
}