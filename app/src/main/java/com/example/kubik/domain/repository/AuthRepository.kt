package com.example.kubik.domain.repository

import com.example.kubik.domain.models.AuthState
import com.example.kubik.domain.models.Group
import com.example.kubik.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginWithVK(userId: Long, firstName: String, lastName: String): Result<User>
    suspend fun getCurrentUser(): User?
    suspend fun checkHasCurrentSession(): Boolean
    suspend fun logout()
    fun observeAuthState(): Flow<AuthState> // Поток статусов для NavHost
    suspend fun updateUserProfile(firstName: String, lastName: String)

    // Методы для онбординга и firestore
    suspend fun getAllGroups(): List<Group>
    suspend fun getUserProfileFromFirestore(userId: String): User?
    suspend fun registerStudent(userId: String, firstName: String, lastName: String, groupId: String): Result<Unit>
    suspend fun registerStarosta(userId: String, firstName: String, lastName: String, inviteCode: String): Result<Unit>
    suspend fun getGroupById(groupId: String): Group?

}