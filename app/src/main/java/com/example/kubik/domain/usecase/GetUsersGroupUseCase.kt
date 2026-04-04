package com.example.kubik.domain.usecase

import com.example.kubik.domain.models.User
import com.example.kubik.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersGroupUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(groupId: String): Flow<List<User>> = authRepository.getUsersGroup(groupId)

}