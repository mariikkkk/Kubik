package com.example.kubik.domain.usecase

import com.example.kubik.domain.repository.AuthRepository
import javax.inject.Inject

class GetGroupByIdUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(groupId: String) = authRepository.getGroupById(groupId)

}