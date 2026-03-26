package com.example.kubik.domain.usecase

import com.example.kubik.domain.models.ThemeMode
import com.example.kubik.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SetThemeModeUseCase  @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
){
    suspend operator fun invoke(themeMode: ThemeMode){
        return userPreferencesRepository.setThemeMode(themeMode = themeMode)
    }
}