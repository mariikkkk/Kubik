package com.example.kubik.domain.queues.models

enum class SlotStatus(val value: String){
    WAITING("waiting"),
    PASSED("passed"),
    FAILED("failed");
    companion object{
        fun fromValue(value: String): SlotStatus{
            return SlotStatus.entries.find { it.value == value } ?: WAITING
        }
    }
}