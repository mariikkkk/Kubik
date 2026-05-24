package com.example.kubik.domain.questions.models

enum class QuestionStatus(val value: String) {
    WAITING("waiting"),
    ANSWERED("answered"),
    RESOLVED("resolved");
    companion object{
        fun fromValue(value: String): QuestionStatus{
            return entries.find { it.value == value } ?: WAITING
        }
    }
}