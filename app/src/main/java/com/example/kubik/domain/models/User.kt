package com.example.kubik.domain.models

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val group: String = "Пока не в группе",
    val role: String = "Студент",
    val avatarUrl: String? = null,
    val vkId: Long? = null
)
