package com.example.kubik.domain.usecase

import com.example.kubik.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class GetSelectedSemesterUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke() = userPreferencesRepository.selectedSemesterFlow
}