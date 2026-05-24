package com.example.kubik.domain.queues.models

data class QueueItem(
    val id: String = "",
    val groupId: String = "",               // Для привязки к конкретной группе
    val title: String = "",                 // Название очереди
    val createdAt: Long = 0L,               // Дата создания
    val closedAt: Long? = null,             // Дата закрытия (может быть null)
    val status: String = QueueStatus.WAITING.value,         // Статус: "waiting", "active", "closed"
    val totalSlots: Int = 0,                // Общее количество мест в очереди
    val currentActiveSlot: Int? = null,     // Кто сдает прямо сейчас
    val creatorId: String = "",              // Id создателя очереди
    val participantIds: List<String> = emptyList(),  // Список всех Id в очереди
    val submissionDate: Long? = null,                // Дата сдачи,
    val userSlots: Map<String, Int> = emptyMap()    //  Словарь всех записавшихся
){
    val typedStatus: QueueStatus
        get() = QueueStatus.fromValue(status)
}
