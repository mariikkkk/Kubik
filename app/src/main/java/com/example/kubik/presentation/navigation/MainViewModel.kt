package com.example.kubik.presentation.navigation

import androidx.lifecycle.ViewModel
import com.example.kubik.domain.models.ThemeMode
import com.example.kubik.domain.usecase.ObserveAuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val observeAuthStateUseCase: ObserveAuthStateUseCase
): ViewModel() {
    val authState = observeAuthStateUseCase()
    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    fun updateThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }
}
