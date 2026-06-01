package com.example.kubik.domain.announcement.usecase

import com.example.kubik.domain.announcement.repository.AnnouncementRepository
import javax.inject.Inject

class DeleteAnnouncementUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository
) {
    suspend operator fun invoke(
        announcementId: String
    ): Result<Unit> {
        return announcementRepository.deleteAnnouncement(announcementId)
    }
}