package com.example.kubik.presentation.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubik.domain.models.Group
import com.example.kubik.domain.usecase.GetAllGroupsUseCase
import com.example.kubik.domain.usecase.GetCurrentUserUseCase
import com.example.kubik.domain.usecase.RegisterStarostaUseCase
import com.example.kubik.domain.usecase.RegisterStudentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getAllGroupsUseCase: GetAllGroupsUseCase,
    private val registerStudentUseCase: RegisterStudentUseCase,
    private val registerStarostaUseCase: RegisterStarostaUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel()  {
    private val _firstName = MutableStateFlow("")
    val firstName = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName = _lastName.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _availableGroups = MutableStateFlow<List<Group>>(emptyList())
    val availableGroups = _availableGroups.asStateFlow()

    init{
        loadGroup()
        fetchUserData()
    }

    private fun loadGroup(){
        viewModelScope.launch {
            val result = getAllGroupsUseCase()
            result.onSuccess{ groups ->
                _availableGroups.value = groups
            }.onFailure{ e ->
                Log.e("DEBUG_KUBIK", "Ошибка загрузки групп: ${e.message}")
            }
        }
    }

    private fun fetchUserData(){
        viewModelScope.launch {
            val userRes = getCurrentUserUseCase()
            userRes.onSuccess { user ->
                if(user != null){
                    _firstName.value = user.firstName
                    _lastName.value = user.lastName
                }
            }.onFailure { e ->
                Log.e("DEBUG_KUBIK", "Ошибка загрузки данных пользователя: ${e.message}")
            }

        }
    }

    fun updateUser(firstName: String, lastName: String){
        _firstName.value = firstName
        _lastName.value = lastName
    }

    fun submitStudent(groupName: String, onSuccess: () -> Unit, onError: (String) -> Unit){
        viewModelScope.launch {
            _isLoading.value = true
            val selectedGroup = availableGroups.value.find { it.name.equals(groupName, ignoreCase = true) }
            val userRes = getCurrentUserUseCase()
            if(selectedGroup == null){
                onError("Группа не найдена! Проверьте правильность названия")
                _isLoading.value = false
                return@launch
            }
            userRes.onSuccess { user ->
                if(user != null){
                    val result = registerStudentUseCase(user.id, _firstName.value, _lastName.value, selectedGroup.id)
                    result.onSuccess {
                        onSuccess()
                    }.onFailure { exception ->
                        onError("Ошибка регистрации студента: ${exception.message}")
                    }
                } else{
                    onError("Ошибка получения данных пользователя")
                }
            }.onFailure { exception ->
                onError("Сбой получения данных пользователя: ${exception.message}")
            }
            _isLoading.value = false
        }

    }
    fun submitStarosta(inviteCode: String, onSuccess: () -> Unit, onError: (String) -> Unit){
        viewModelScope.launch {
            _isLoading.value = true
            val userRes = getCurrentUserUseCase()
            userRes.onSuccess { user ->
                if(user != null) {
                    val result = registerStarostaUseCase(user.id, _firstName.value, _lastName.value, inviteCode)
                    result.onSuccess {
                        onSuccess()
                    }.onFailure { exception ->
                        onError("Ошибка регистрации старосты: ${exception.message}")
                    }
                }
            }.onFailure { exception ->
                onError("Сбой получения данных пользователя: ${exception.message}")
            }
            _isLoading.value = false

        }

    }

}