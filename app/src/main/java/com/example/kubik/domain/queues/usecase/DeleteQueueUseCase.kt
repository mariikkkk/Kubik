package com.example.kubik.domain.queues.usecase

import com.example.kubik.domain.queues.repository.QueuesRepository
import javax.inject.Inject

class DeleteQueueUseCase @Inject constructor(
    private val queuesRepository: QueuesRepository
){
    suspend operator fun invoke(queueId: String): Result<Unit> {
        return queuesRepository.deleteQueue(queueId)
    }
}