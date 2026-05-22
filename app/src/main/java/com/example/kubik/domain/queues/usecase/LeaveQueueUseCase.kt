package com.example.kubik.domain.queues.usecase

import com.example.kubik.domain.queues.repository.QueuesRepository
import javax.inject.Inject

class LeaveQueueUseCase @Inject constructor(
    private val queuesRepository: QueuesRepository
) {
    suspend operator fun invoke(
        queueId: String,
        slotNumber: Int,
        currentActiveSlot: Int?
    ): Result<Unit> = runCatching{
        queuesRepository.leaveQueue(queueId, slotNumber)
        if(currentActiveSlot == slotNumber){
            val nextSlot = queuesRepository.getFirstWaitingSlot(queueId).getOrThrow()
            queuesRepository.updateQueue(queueId, mapOf("currentActiveSlot" to nextSlot))
        }
    }
}