package com.example.kubik.domain.usecase

import com.example.kubik.domain.models.User
import com.example.kubik.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserProfile @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(userId: String): Flow<User?> {
        return authRepository.observeUserProfile(userId)
    }

}