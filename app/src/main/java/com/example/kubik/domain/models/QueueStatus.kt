package com.example.kubik.domain.models

enum class QueueStatus(val value: String){
    WAITING("waiting"),
    ACTIVE("active"),
    CLOSED("closed");
    companion object{
        fun fromValue(value: String): QueueStatus{
            return QueueStatus.entries.find { it.value == value } ?: WAITING
        }
    }
}