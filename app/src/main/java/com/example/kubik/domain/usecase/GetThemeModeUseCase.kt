package com.example.kubik.domain.usecase

import com.example.kubik.domain.models.ThemeMode
import com.example.kubik.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetThemeModeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
     operator fun invoke(): Flow<ThemeMode> {
        return userPreferencesRepository.themeModeFlow
    }
}