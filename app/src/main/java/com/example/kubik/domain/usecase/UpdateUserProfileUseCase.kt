package com.example.kubik.domain.usecase

import com.example.kubik.domain.repository.AuthRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val getUserUseCase: GetUserUseCase,
    private val saveUserUseCase: SaveUserUseCase
) {
    suspend operator fun invoke(firstName: String, lastName: String): Result<Unit> {
        val res = authRepository.updateUserProfile(firstName, lastName)
        res.onSuccess {
            val currentUser = getUserUseCase().firstOrNull()

            if(currentUser != null){
                val updatedUser = currentUser.copy(firstName = firstName, lastName = lastName)
                saveUserUseCase(updatedUser)
            }
        }
        return res
    }
}