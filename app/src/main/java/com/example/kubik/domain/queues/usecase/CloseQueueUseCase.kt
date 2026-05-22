package com.example.kubik.domain.queues.usecase

import com.example.kubik.domain.queues.models.QueueItem
import com.example.kubik.domain.queues.models.QueueStatus
import com.example.kubik.domain.queues.models.SlotItem
import com.example.kubik.domain.queues.models.SlotStatus
import com.example.kubik.domain.queues.repository.QueuesRepository
import javax.inject.Inject

class CloseQueueUseCase @Inject constructor(
    private val queuesRepository: QueuesRepository
){
    suspend operator fun invoke(
        oldQueue: QueueItem,
        allSlots: List<SlotItem>,
        shouldMigrate: Boolean,
        newTotalSlots: Int,
        newSubmissionDate: Long = oldQueue.submissionDate?.plus(7L * 24 * 60 * 60 * 1000)
            ?: requireNotNull(oldQueue.submissionDate){"submissionDate is null for queue ${oldQueue.id}"}
    ): Result<Unit> {
        val waitingSlots = allSlots.filter { it.typedStatus == SlotStatus.WAITING }
        if(!shouldMigrate){
            waitingSlots.forEach { slot ->
                queuesRepository.updateSlotStatus(
                    slot.slotNumber,
                    queueId = oldQueue.id,
                    SlotStatus.FAILED.value
                ).getOrThrow()
            }
            return queuesRepository.updateQueue(
                oldQueue.id,
                mapOf(
                    "status" to QueueStatus.CLOSED.value,
                    "closedAt" to System.currentTimeMillis()
                )
            )
        }
//        val studentsToMigrate = waitingSlots.map {
//            it.copy(status = SlotStatus.FAILED.value)
//        }
        val studentsToMigrate = allSlots.filter {
            it.typedStatus == SlotStatus.WAITING
        }
        val newQueue = QueueItem(
            id = "",
            groupId = oldQueue.groupId,
            title = oldQueue.title,
            createdAt = System.currentTimeMillis(),
            status = QueueStatus.WAITING.value,
            totalSlots = newTotalSlots,
            currentActiveSlot = null,
            creatorId = oldQueue.creatorId,
            submissionDate = newSubmissionDate
        )
        waitingSlots.forEach { slot ->
            queuesRepository.updateSlotStatus(
                slot.slotNumber,
                queueId = oldQueue.id,
                SlotStatus.FAILED.value
            ).getOrThrow()
        }
        return queuesRepository.closeAndMigrateQueue(
            oldQueue.id,
            newQueue,
            studentsToMigrate
        )
    }
}