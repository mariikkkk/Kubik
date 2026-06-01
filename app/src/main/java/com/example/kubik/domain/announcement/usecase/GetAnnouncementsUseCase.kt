package com.example.kubik.domain.announcement.usecase

import com.example.kubik.domain.announcement.model.AnnouncementItem
import com.example.kubik.domain.announcement.repository.AnnouncementRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnnouncementsUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository
){
    operator fun invoke(groupId: String): Flow<List<AnnouncementItem>> {
        return announcementRepository.getAnnouncements(groupId)
    }
}