package com.example.kubik.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    var email by mutableStateOf("")
    private set
    var password by mutableStateOf("")
        private set
    var passwordVisible by mutableStateOf(false) // Скрытие пароля
        private set

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