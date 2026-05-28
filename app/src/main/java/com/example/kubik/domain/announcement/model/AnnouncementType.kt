package com.example.kubik.domain.announcement.model

enum class AnnouncementType(val value: String) {
    NORMAL("normal"),
    IMPORTANT("important");
    companion object{
        fun fromValue(value: String): AnnouncementType{
            return entries.find { it.value == value } ?: NORMAL
        }
    }
}