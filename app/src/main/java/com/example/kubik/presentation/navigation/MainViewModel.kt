package com.example.kubik.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kubik.domain.models.AppState
import com.example.kubik.domain.models.AuthState
import com.example.kubik.domain.models.ThemeMode
import com.example.kubik.domain.repository.UserPreferencesRepository
import com.example.kubik.domain.usecase.GetCurrentUserUseCase
import com.example.kubik.domain.usecase.GetThemeModeUseCase
import com.example.kubik.domain.usecase.GetUserProfileFromFirestoreUseCase
import com.example.kubik.domain.usecase.ObserveAuthStateUseCase
import com.example.kubik.domain.usecase.SetThemeModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val getThemeModeUseCase: GetThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserProfileFromFirestoreUseCase: GetUserProfileFromFirestoreUseCase
): ViewModel() {

    private val _appState = MutableStateFlow<AppState>(AppState.Loading)
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    val authState = observeAuthStateUseCase()

    init {
        checkUserStatus()
    }
    fun checkUserStatus(){
        viewModelScope.launch {
            observeAuthStateUseCase().collect { authState ->
                when(authState){
                    is AuthState.Loading -> {
                        _appState.value = AppState.Loading
                    }
                    is AuthState.Unauthenticated -> {
                        _appState.value = AppState.Login
                    }
                    is AuthState.Authenticated -> {
                        val userRes = getCurrentUserUseCase()
                        userRes.onSuccess { user ->
                            if (user != null){
                                val userId = user.id
                                try {
                                    val firestoreProfileRes = getUserProfileFromFirestoreUseCase(userId)
                                    firestoreProfileRes.onSuccess { firestoreProfile ->
                                        if (firestoreProfile?.groupId != null) {
                                            _appState.value = AppState.Home
                                        } else {
                                            _appState.value = AppState.Onboarding
                                        }
                                    }.onFailure { e ->
                                        e.printStackTrace()
                                        _appState.value = AppState.Onboarding
                                    }
                                } catch (e: Exception){
                                    e.printStackTrace()
                                    _appState.value = AppState.Onboarding
                                }
                            } else{
                                _appState.value = AppState.Login
                            }
                        }.onFailure { e ->
                            e.printStackTrace()
                            _appState.value = AppState.Login
                        }

                    }
                }
            }
        }
    }
    val themeMode = getThemeModeUseCase().stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = ThemeMode.SYSTEM
    )

    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            setThemeModeUseCase(mode)
        }
    }
}
