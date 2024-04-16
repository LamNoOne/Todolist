package com.example.todolist.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    val title: String,
    val description: String?,
    val isDone: Boolean,
    val timestamp: String ?= "CURRENT_TIMESTAMP",
    @PrimaryKey val id: Int? = null
)