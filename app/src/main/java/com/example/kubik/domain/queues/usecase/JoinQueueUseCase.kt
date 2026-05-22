    package com.example.kubik.domain.queues.usecase

    import com.example.kubik.domain.queues.models.QueueStatus
    import com.example.kubik.domain.queues.repository.QueuesRepository
    import javax.inject.Inject

    class JoinQueueUseCase @Inject constructor(
        private val queuesRepository: QueuesRepository
    ) {
        suspend operator fun invoke(
            queueId: String,
            slotNumber: Int,
            userId: String,
            userName: String,
            prevSlotNumber: Int? = null,
            queueStatus: QueueStatus,
            currentActiveSlot: Int?
        ): Result<Unit> {
            if(queueStatus == QueueStatus.CLOSED){
                return Result.failure(Exception("Очередь закрыта"))
            }
            if (prevSlotNumber != null) {
                val leaveResult = queuesRepository.leaveQueue(queueId, prevSlotNumber)
                if (leaveResult.isFailure) return leaveResult
            }
            val joinResult = queuesRepository.joinQueueTransaction(
                queueId, slotNumber, userId, userName
            )
            if (joinResult.isFailure) return joinResult
            if (queueStatus == QueueStatus.ACTIVE
                && (currentActiveSlot == null || currentActiveSlot == prevSlotNumber)) {
                val nextSlot = queuesRepository.getFirstWaitingSlot(queueId).getOrThrow()
                return queuesRepository.updateQueue(
                    queueId,
                    mapOf("currentActiveSlot" to nextSlot?.slotNumber)
                )
            }
            return joinResult
        }
    }