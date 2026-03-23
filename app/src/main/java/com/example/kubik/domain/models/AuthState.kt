package com.example.kubik.domain.models

sealed class AuthState {
    object Loading: AuthState()
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
}