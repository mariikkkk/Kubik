package com.example.kubik.domain.models

data class QueueSlot(
    val id: Int, // номер ячейки
    val studentName: String? = null, // Имя студента или null, если ячейка свободна
    val isMySlot: Boolean = false // true, если ячейка принадлежит текущему студенту
)
