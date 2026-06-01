package com.example.kubik.domain.announcement.usecase

import com.example.kubik.domain.announcement.model.AnnouncementItem
import com.example.kubik.domain.announcement.model.AnnouncementType
import com.example.kubik.domain.announcement.repository.AnnouncementRepository
import javax.inject.Inject

class CreateAnnouncementUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository
) {
    suspend operator fun invoke(
        groupId: String,
        authorId: String,
        authorName: String,
        text: String,
        title: String,
        type: AnnouncementType
    ): Result<Unit>{
        val titleTrimmed = title.trim()
        val textTrimmed = text.trim()
        if(titleTrimmed.isBlank()){
            return Result.failure(Exception("Объявление не может быть пустым"))
        }
        if(textTrimmed.isBlank()){
            return Result.failure(Exception("Название не может быть пустым"))
        }
        val announcement = AnnouncementItem(
            id = "",
            groupId = groupId,
            title = title,
            authorId = authorId,
            authorName = authorName,
            text = text,
            createdAt = System.currentTimeMillis(),
            type = type.value
        )
        return announcementRepository.createAnnouncement(announcement)
    }
}