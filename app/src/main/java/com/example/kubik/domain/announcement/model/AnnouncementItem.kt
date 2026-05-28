package com.example.kubik.domain.announcement.model

data class AnnouncementItem(
    val id: String = "",
    val groupId: String = "",
    val title: String = "",
    val text: String = "",
    val createdAt: Long = 0L,
    val type: String = AnnouncementType.NORMAL.value,
    val authorName: String = "",
    val authorId: String = ""
)