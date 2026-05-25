package com.example.kubik.presentation.home

import android.util.Log
import androidx.compose.animation.core.updateTransition
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubik.domain.models.User
import com.example.kubik.domain.usecase.ClearUserUseCase
import com.example.kubik.domain.usecase.GetCurrentUserUseCase
import com.example.kubik.domain.usecase.GetGroupByIdUseCase
import com.example.kubik.domain.usecase.GetUserProfileFromFirestoreUseCase
import com.example.kubik.domain.usecase.GetUserUseCase
import com.example.kubik.domain.usecase.GetUsersGroupUseCase
import com.example.kubik.domain.usecase.LogoutUseCase
import com.example.kubik.domain.usecase.SaveUserUseCase
import com.example.kubik.domain.usecase.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val clearUserUseCase: ClearUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val getGroupByIdUseCase: GetGroupByIdUseCase,
    private val getUserProfileFromFirestoreUseCase: GetUserProfileFromFirestoreUseCase,
    private val getUsersGroupUseCase: GetUsersGroupUseCase
): ViewModel()   {
    val userState: StateFlow<User?> = getUserUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    private val _groupName = MutableStateFlow("Загрузка...")
    val groupName = _groupName.asStateFlow()

    private val _pendingUsersCount = MutableStateFlow(0)
    val pendingUsersCount: StateFlow<Int> = _pendingUsersCount.asStateFlow()


    init {
        syncUser()
        viewModelScope.launch {
            userState.collect { user ->
                if (user?.groupId != null) {
                    fetchGroupName(user.groupId)
                    getUsersGroupUseCase(user.groupId).collect { members ->
                        _pendingUsersCount.value = members.count { it.status == "pending" }
                    }
                }
            }
        }

    }
    fun syncUser() {
        viewModelScope.launch {
            val currentUserRes = getCurrentUserUseCase()
            currentUserRes.onSuccess { currentUser ->
                if (currentUser != null) {
                    val fullProfileRes = getUserProfileFromFirestoreUseCase(currentUser.id)
                    fullProfileRes.onSuccess { fullProfile ->
                        if (fullProfile != null && fullProfile.groupId != null) {
                            saveUserUseCase(fullProfile)
                        } else {
                            saveUserUseCase(currentUser)
                        }
                    }.onFailure {
                        Log.e("DEBUG_KUBIK", "Ошибка загрузки профиля из Firestore")
                        saveUserUseCase(currentUser)
                    }
                }
            }.onFailure { e ->
                Log.e("DEBUG_KUBIK", "Ошибка синхронизации: ${e.message}")
            }
        }
    }

    fun fetchGroupName(groupId: String){
        Log.d("DEBUG_KUBIK", "Пытаюсь найти группу с ID: '$groupId'")
        viewModelScope.launch {
            val groupRes = getGroupByIdUseCase(groupId)
            groupRes.onSuccess { group ->
                if(group != null){
                    _groupName.value = group.name
                } else{
                    _groupName.value = "Группа не найдена"
                }
            }.onFailure { e ->
                Log.e("DEBUG_KUBIK", "Ошибка загрузки имени группы: ${e.message}")
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
            val res = logoutUseCase()
            res.onFailure { e ->
                Log.e("DEBUG_KUBIK", "Ошибка выхода: ${e.message}")
            }
            try{
                clearUserUseCase()
                withContext(Dispatchers.Main){
                    onSuccess()
                }
            } catch (e: Exception){
                Log.e("DEBUG_KUBIK", "Ошибка очистки локальных данных: ${e.message}")
            }
        }
    }
}