package com.example.kubik.domain.usecase

import com.example.kubik.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterStudentUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        userId: String,
        firstName: String,
        lastName: String,
        groupId: String
    ): Result<Unit> {
        return authRepository.registerStudent(userId, firstName, lastName, groupId)
    }
}