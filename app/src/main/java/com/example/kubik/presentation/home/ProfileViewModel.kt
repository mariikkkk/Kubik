package com.example.kubik.presentation.home

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubik.domain.models.User
import com.example.kubik.domain.usecase.GetCurrentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUseCase: GetCurrentUseCase
): ViewModel()   {
    private val _userState = MutableStateFlow<User?>(null)
    val userState = _userState.asStateFlow()

    init {
        loadUser()
    }
    fun loadUser(){
        viewModelScope.launch{
            _userState.value = getCurrentUseCase()
        }
    }
}