package com.example.kubik.domain.usecase

import com.example.kubik.domain.models.User
import com.example.kubik.domain.repository.AuthRepository
import javax.inject.Inject

class AuthWithVKUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(userId: Long, firstName: String, lastName: String): Result<User> {
        return authRepository.loginWithVK(userId, firstName, lastName)
    }
}