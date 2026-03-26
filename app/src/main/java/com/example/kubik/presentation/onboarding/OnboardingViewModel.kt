package com.example.kubik.presentation.onboarding

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
        viewModelScope.launch {
            _availableGroups.value = getAllGroupsUseCase()
        }
        fetchUserData()
    }

    private fun fetchUserData(){
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            if(user != null){
                _firstName.value = user.firstName
                _lastName.value = user.lastName
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
            val user = getCurrentUserUseCase()
            if(selectedGroup == null){
                onError("Группа не найдена! Проверьте правильность названия")
            }
            else if(user != null){
                val result = registerStudentUseCase(user.id, _firstName.value, _lastName.value, selectedGroup.id)
                if(result.isSuccess){
                    onSuccess()
            } else{
                onError("Ощибка регистрации студента")
            }
                _isLoading.value = false
            }
        }

    }
    fun submitStarosta(inviteCode: String, onSuccess: () -> Unit, onError: (String) -> Unit){
        viewModelScope.launch {
            _isLoading.value = true
            val user = getCurrentUserUseCase()
            if(user != null) {
                val result = registerStarostaUseCase(user.id, _firstName.value, _lastName.value, inviteCode)
                if (result.isSuccess) {
                    onSuccess()
                } else {
                    onError("Ошибка регистрации старосты")
                }
                _isLoading.value = false
            }
        }

    }

}