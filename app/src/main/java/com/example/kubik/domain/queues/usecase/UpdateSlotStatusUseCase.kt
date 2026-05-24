package com.example.kubik.domain.queues.usecase

import com.example.kubik.domain.queues.models.SlotStatus
import com.example.kubik.domain.queues.repository.QueuesRepository
import javax.inject.Inject

class UpdateSlotStatusUseCase @Inject constructor(
    private val queuesRepository: QueuesRepository
){
    suspend operator fun invoke(
        queueId: String,
        slotNumber: Int,
        newStatus: SlotStatus
    ): Result<Unit> = runCatching {
        queuesRepository.updateSlotStatus(slotNumber, queueId, newStatus.value).getOrThrow()
        val nextSlot = queuesRepository.getFirstWaitingSlot(queueId).getOrThrow()
        queuesRepository.updateQueue(queueId, mapOf(
            "currentActiveSlot" to nextSlot?.slotNumber)
        ).getOrThrow()
    }
}