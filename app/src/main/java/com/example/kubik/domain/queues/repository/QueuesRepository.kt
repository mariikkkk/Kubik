package com.example.kubik.domain.queues.repository

import com.example.kubik.domain.queues.models.QueueItem
import com.example.kubik.domain.queues.models.SlotItem
import kotlinx.coroutines.flow.Flow

interface QueuesRepository {
    fun getQueues(groupId: String): Flow<List<QueueItem>>               // Получение списка очередей для конкретной группы
    fun getQueueSlots(queueId: String): Flow<List<SlotItem>>            // Получение списка мест для конкретной очереди)
    suspend fun createQueue(queue: QueueItem): Result<Unit>                           // Создание новой очереди
    suspend fun joinQueueTransaction(
        queueId: String,
        slotNumber: Int,
        userId: String,
        userName: String
    ): Result<Unit>                                                                    // Бронирование места
    suspend fun updateSlotStatus(
        slotNumber: Int,
        queueId: String,
        newStatus: String
    ): Result<Unit>                                                                    // Обновление статуса места
    suspend fun updateQueue(queueId: String, updates: Map<String, Any?>): Result<Unit> // Обновление данных о очереди
    suspend fun leaveQueue(queueId: String, slotNumber: Int): Result<Unit>             // Покинуть очередь
    suspend fun deleteQueue(queueId: String): Result<Unit>                             // Удалить очередь
    suspend fun closeAndMigrateQueue(
        oldQueueId: String,
        newQueue: QueueItem,
        migratedSlots: List<SlotItem>
    ): Result<Unit>                                                                    // Закрыть очередь
    suspend fun getFirstWaitingSlot(queueId: String): Result<SlotItem?>                // Получение первого ожидающего слота для старта очереди
    fun getQueueById(queueId: String): Flow<QueueItem?> // Получение очереди по ID
    suspend fun getQueueSlotsOnce(queueId: String): List<SlotItem>  // Получение списка слотов в очереди (не подписка)
}