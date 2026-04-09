package com.example.kubik.domain.models

data class SlotItem(
    val slotNumber: Int = 0,                                // Номер места
    val userId: String = "",                                // Id пользователя, который забронировал
    val userName: String = "",                              // Имя пользователя
    val status: String = SlotStatus.WAITING.value           // Статус места: "waiting", "active", "closed"
){
    val typedStatus: SlotStatus
        get() = SlotStatus.fromValue(status)
}
