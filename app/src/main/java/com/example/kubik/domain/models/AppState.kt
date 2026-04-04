package com.example.kubik.domain.models

sealed class AppState {
    object Loading : AppState()
    object Login : AppState()
    object Onboarding : AppState()
    object Pending : AppState()
    object Home : AppState()
}