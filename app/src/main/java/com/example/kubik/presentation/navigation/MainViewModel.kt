package com.example.kubik.presentation.navigation

import androidx.lifecycle.ViewModel
import com.example.kubik.domain.usecase.ObserveAuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val observeAuthStateUseCase: ObserveAuthStateUseCase
): ViewModel() {
    val authState = observeAuthStateUseCase()
}