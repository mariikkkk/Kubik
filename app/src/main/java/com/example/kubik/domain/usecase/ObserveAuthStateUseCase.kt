package com.example.kubik.domain.usecase

import com.example.kubik.domain.models.AuthState
import com.example.kubik.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<AuthState>{
        return authRepository.observeAuthState()
    }
}