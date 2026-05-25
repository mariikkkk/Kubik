package com.example.kubik.domain.models

data class Group(
    val id: String = "",
    val name: String = "",
    val inviteCode: String ="",
    val starostaId: String? = null
)