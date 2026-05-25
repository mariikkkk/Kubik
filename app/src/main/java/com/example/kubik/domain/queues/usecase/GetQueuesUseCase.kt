package com.example.kubik.domain.queues.usecase

import com.example.kubik.domain.queues.models.QueueItem
import com.example.kubik.domain.queues.repository.QueuesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQueuesUseCase @Inject constructor(
    private val queuesRepository: QueuesRepository
){
    operator fun invoke(groupId: String): Flow<List<QueueItem>> {
        return queuesRepository.getQueues(groupId)
    }
}