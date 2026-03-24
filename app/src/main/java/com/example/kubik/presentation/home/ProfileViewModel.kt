package com.example.kubik.presentation.home

import android.util.Log
import androidx.compose.animation.core.updateTransition
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubik.domain.models.User
import com.example.kubik.domain.usecase.ClearUserUseCase
import com.example.kubik.domain.usecase.GetCurrentUserUseCase
import com.example.kubik.domain.usecase.GetUserUseCase
import com.example.kubik.domain.usecase.LogoutUseCase
import com.example.kubik.domain.usecase.SaveUserUseCase
import com.example.kubik.domain.usecase.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val clearUserUseCase: ClearUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
): ViewModel()   {
    val userState: StateFlow<User?> = getUserUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    init {
        syncUser()
    }
    fun syncUser(){
        viewModelScope.launch{
            val currentUser = getCurrentUserUseCase()
            if(currentUser != null){
                saveUserUseCase(currentUser)
            }
        }
    }

    fun updateUserProfile(firstName: String, lastName: String){
        viewModelScope.launch {
            try{
                updateUserProfileUseCase(firstName, lastName)
            } catch (e: Exception){
                Log.e("ProfileViewModel", "Error updating user profile", e)
            }

        }
    }
    fun logout(onSuccess: () -> Unit){
        viewModelScope.launch {
            try{
                logoutUseCase()
                clearUserUseCase()
                onSuccess()
            } catch (e: Exception){
                Log.e("ProfileViewModel", "Error logging out", e)
            }

        }

    }
}