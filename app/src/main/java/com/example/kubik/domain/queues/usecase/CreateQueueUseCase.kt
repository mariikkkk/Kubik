package com.example.kubik.domain.queues.usecase

import com.example.kubik.domain.queues.models.QueueItem
import com.example.kubik.domain.queues.models.QueueStatus
import com.example.kubik.domain.queues.repository.QueuesRepository
import javax.inject.Inject

class CreateQueueUseCase @Inject constructor(
    private val queuesRepository: QueuesRepository
) {
    suspend operator fun invoke(
        title: String,
        totalSlots: Int,
        groupId: String,
        creatorId: String,
        submissionDate: Long?
    ): Result<Unit> {
        val queue = QueueItem(
            id = "",
            title = title,
            totalSlots = totalSlots,
            createdAt = System.currentTimeMillis(),
            groupId = groupId,
            creatorId = creatorId,
            status = QueueStatus.WAITING.value,
            submissionDate = submissionDate
        )
        return queuesRepository.createQueue(queue)
    }
}