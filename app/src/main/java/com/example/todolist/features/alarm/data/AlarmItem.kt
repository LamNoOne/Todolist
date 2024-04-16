package com.example.todolist.features.alarm.data

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val message: String
)