package com.example.kubik.di

import com.example.kubik.data.AnnouncementRepositoryImpl
import com.example.kubik.domain.announcement.repository.AnnouncementRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AnnouncementModule {
    @Binds
    abstract fun bindAnnouncementRepository(
        announcementRepositoryImpl: AnnouncementRepositoryImpl
    ): AnnouncementRepository
}