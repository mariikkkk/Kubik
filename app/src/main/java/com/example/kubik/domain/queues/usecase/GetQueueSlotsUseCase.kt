package com.example.kubik.domain.queues.usecase

import com.example.kubik.domain.queues.repository.QueuesRepository
import javax.inject.Inject

class GetQueueSlotsUseCase @Inject constructor(
    private val queuesRepository: QueuesRepository
) {
    operator fun invoke(queueId: String) = queuesRepository.getQueueSlots(queueId)
}