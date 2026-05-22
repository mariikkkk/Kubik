package com.example.kubik.domain.queues.usecase

import com.example.kubik.domain.queues.models.QueueItem
import com.example.kubik.domain.queues.repository.QueuesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQueueByIdUseCase @Inject constructor(
    private val queuesRepository: QueuesRepository
) {
    operator fun invoke(queueId: String): Flow<QueueItem?> {
        return queuesRepository.getQueueById(queueId)
    }
}