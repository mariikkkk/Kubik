package com.example.kubik.domain.queues.usecase

import com.example.kubik.domain.queues.repository.QueuesRepository
import javax.inject.Inject

class UpdateQueueUseCase @Inject constructor(
    private val queuesRepository: QueuesRepository
) {
    suspend operator fun invoke(
        queueId: String,
        newTitle: String,
        newTotalSlots: Int): Result<Unit> = runCatching {
        val slots = queuesRepository.getQueueSlotsOnce(queueId)
        val maxOcupSlot = slots.maxOfOrNull { it.slotNumber } ?: 0
        if (newTotalSlots < maxOcupSlot) {
            throw Exception("Нельзя уменьшить количество слотов ниже занятого места")
        }
        queuesRepository.updateQueue(
            queueId,
            mapOf(
            "title" to newTitle,
            "totalSlots" to newTotalSlots
            )
        ).getOrThrow()
    }
}