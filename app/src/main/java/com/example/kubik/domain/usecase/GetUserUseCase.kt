package com.example.kubik.domain.usecase

import com.example.kubik.domain.models.User
import com.example.kubik.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
)
{
    operator fun invoke(): Flow<User?> {
        return userPreferencesRepository.userFlow
    }
}