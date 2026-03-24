package com.example.kubik.domain.repository

import com.example.kubik.domain.models.AuthState
import com.example.kubik.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginWithVK(userId: Long, firstName: String, lastName: String): Result<User>
    suspend fun getCurrentUser(): User?
    suspend fun checkHasCurrentSession(): Boolean
    suspend fun logout()
    fun observeAuthState(): Flow<AuthState> // Поток статусов для NavHost
    suspend fun updateUserProfile(firstName: String, lastName: String)
}