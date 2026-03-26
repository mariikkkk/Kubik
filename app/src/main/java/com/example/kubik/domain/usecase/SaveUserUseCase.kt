package com.example.kubik.domain.usecase

import com.example.kubik.domain.models.User
import com.example.kubik.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(user: User) {
        userPreferencesRepository.saveUser(user)
    }
}
