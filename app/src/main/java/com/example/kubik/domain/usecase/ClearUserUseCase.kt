package com.example.kubik.domain.usecase

import com.example.kubik.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class ClearUserUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(){
        userPreferencesRepository.deleteUser()
    }
}