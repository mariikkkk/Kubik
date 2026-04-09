package com.example.kubik.domain.models

enum class SlotStatus(val value: String){
    WAITING("waiting"),
    ACTIVE("active"),
    CLOSED("closed");
    companion object{
        fun fromValue(value: String): SlotStatus{
            return SlotStatus.entries.find { it.value == value } ?: WAITING
        }
    }
}