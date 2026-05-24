package com.example.kubik.domain.usecase

import com.example.kubik.domain.models.User
import com.example.kubik.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserProfileFromFirestoreUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userId: String): Result<User?> = authRepository.getUserProfileFromFirestore(userId)
}