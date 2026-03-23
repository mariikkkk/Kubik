package com.example.kubik.di

import com.example.kubik.data.SupabaseAuthRepositoryImpl
import com.example.kubik.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: SupabaseAuthRepositoryImpl
    ): AuthRepository

}