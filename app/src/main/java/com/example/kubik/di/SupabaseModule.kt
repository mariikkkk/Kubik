package com.example.kubik.di

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseModule {
    val supabase = createSupabaseClient(
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImR4dnp3aHNxYnNnYnl4bHJjbmliIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzQwODA5OTgsImV4cCI6MjA4OTY1Njk5OH0.jCPe3eCXBGIzjXU54ZrALybfF89NIcATTzYhbirq6Do",
        supabaseUrl = "https://dxvzwhsqbsgbyxlrcnib.supabase.co"
    ){
        install(Postgrest)
        install(Auth)
    }
}