package com.example.kubik.di

import dagger.Module
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.plugins.HttpTimeout
import javax.inject.Singleton

object SupabaseModule {
    @OptIn(SupabaseInternal::class)
    val supabase = createSupabaseClient(
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImR4dnp3aHNxYnNnYnl4bHJjbmliIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzQwODA5OTgsImV4cCI6MjA4OTY1Njk5OH0.jCPe3eCXBGIzjXU54ZrALybfF89NIcATTzYhbirq6Do",
        supabaseUrl = "https://dxvzwhsqbsgbyxlrcnib.supabase.co"
    ) {
        install(Postgrest)
        install(Auth)
        httpConfig {
            install(HttpTimeout) {
                requestTimeoutMillis = 300000  // 5 минут
                connectTimeoutMillis = 300000
                socketTimeoutMillis = 300000
            }

        }
    }
}