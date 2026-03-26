package com.example.kubik.domain.models

data class User(
    val id: String = "",
    val firstName: String ="",
    val lastName: String ="",
    val groupId: String? = null,
    val role: String = "Студент",
    val status: String = "pending",
    val avatarUrl: String? = null,
    val vkId: Long? = null
)
