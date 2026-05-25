package com.example.kubik.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Long.toFormattedDate(
    pattern: String = "d MMMM",
): String{
    val instant = Instant.ofEpochMilli(this)    // Создание объекта времени. Точка на временной шкале
    val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) //Дата и время с привязкой к часовому поясу
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale("ru"))      // Форматирование
    return dateTime.format(formatter)
}