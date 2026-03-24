package com.example.kubik.di

import android.content.Context
import com.example.kubik.data.FirebaseFilesRepositoryImpl
import com.example.kubik.domain.repository.FilesRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class FireBaseModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFilesRepository(
        firestore: FirebaseFirestore,
        @ApplicationContext context: Context): FilesRepository {
        return FirebaseFilesRepositoryImpl(context, firestore)

    }

}