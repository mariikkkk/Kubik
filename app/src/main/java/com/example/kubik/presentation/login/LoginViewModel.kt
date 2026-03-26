package com.example.kubik.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubik.di.SupabaseModule
import com.example.kubik.domain.usecase.AuthWithVKUseCase
import com.example.kubik.domain.usecase.CheckAuthStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authWithVKUseCase: AuthWithVKUseCase,
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)

    var isLoading = _isLoading.asStateFlow()
    var email by mutableStateOf("")
    private set
    var password by mutableStateOf("")
        private set
    var passwordVisible by mutableStateOf(false) // Скрытие пароля
        private set

    fun authWithVk(
        vkUserId: Long,
        firstName: String,
        lastName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit){
        viewModelScope.launch {
            if (checkAuthStatusUseCase()){
                onSuccess()
                return@launch
            }
            _isLoading.value = true
            val result = authWithVKUseCase(vkUserId, firstName, lastName)
            result.onSuccess {
                _isLoading.value = false
                onSuccess()
            }
            result.onFailure { error ->
                _isLoading.value = false
                onError(error.message ?: "Unknown error")
            }

        }
    }
    fun updateEmail(newEmail: String){
        email = newEmail
    }

    fun updatePassword(newPassword: String){
        password = newPassword
    }

    fun togglePasswordVisibility(){
        passwordVisible = !passwordVisible
    }
}