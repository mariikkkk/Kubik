package com.example.kubik.di

import com.example.kubik.data.QueuesRepositoryImpl
import com.example.kubik.domain.queues.repository.QueuesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class QueuesModule {
    @Binds
    abstract fun bindQueuesRepository(
        queuesRepositoryImpl: QueuesRepositoryImpl
    ): QueuesRepository
}