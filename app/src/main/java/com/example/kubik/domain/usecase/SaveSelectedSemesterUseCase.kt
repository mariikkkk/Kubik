package com.example.kubik.domain.usecase

import com.example.kubik.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveSelectedSemesterUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(semester: Int){
        userPreferencesRepository.saveSelectedSemester(semester)
    }
}