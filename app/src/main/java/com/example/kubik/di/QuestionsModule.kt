package com.example.kubik.di

import com.example.kubik.data.QuestionsRepositoryImpl
import com.example.kubik.domain.questions.repository.QuestionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class QuestionsModule {
    @Binds
    abstract fun bindQuestionsRepository(
        questionsRepositoryImpl: QuestionsRepositoryImpl
    ): QuestionsRepository
}