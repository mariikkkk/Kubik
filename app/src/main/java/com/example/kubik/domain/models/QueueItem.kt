package com.example.kubik.domain.models

data class QueueItem(
    val id: Int,
    val title: String,              // Название очереди
    val subtitle: String,           // Доп инфа
    val participantsCount: Int,     // Количество участников
    val myPlace: Int? = null,       // Если null - значит вас нет в очереди и можно записаться
    val status: String              // Статус очереди

)
