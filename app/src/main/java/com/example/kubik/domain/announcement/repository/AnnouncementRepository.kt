package com.example.kubik.domain.announcement.repository

import com.example.kubik.domain.announcement.model.AnnouncementItem
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    fun getAnnouncements(groupId: String): Flow<List<AnnouncementItem>>
    suspend fun createAnnouncement(announcement: AnnouncementItem): Result<Unit>
    suspend fun deleteAnnouncement(announcementId: String): Result<Unit>
}