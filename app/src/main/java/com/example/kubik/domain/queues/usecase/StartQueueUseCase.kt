package com.example.kubik.domain.queues.usecase

import com.example.kubik.domain.queues.models.QueueStatus
import com.example.kubik.domain.queues.repository.QueuesRepository
import java.lang.Exception
import javax.inject.Inject

class StartQueueUseCase @Inject constructor(
    private val queuesRepository: QueuesRepository
) {
    suspend operator fun invoke(
        queueId: String
    ): Result<Unit> {
        val firstSlot = queuesRepository.getFirstWaitingSlot(queueId).getOrElse { error ->
            return Result.failure(error)
        } ?: return Result.failure(Exception("Очередь пуста"))

        return queuesRepository.updateQueue(
            queueId,
            mapOf(
                "status" to QueueStatus.ACTIVE.value,
                "currentActiveSlot" to firstSlot.slotNumber
            )
        )
    }
}