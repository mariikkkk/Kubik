package com.example.kubik.domain.usecase

import com.example.kubik.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterStarostaUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(
        userId: String,
        firstName: String,
        lastName: String,
        inviteCode: String
    ): Result<Unit>{
        return authRepository.registerStarosta(userId, firstName, lastName, inviteCode)
    }
}