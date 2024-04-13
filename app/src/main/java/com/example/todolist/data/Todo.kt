package com.example.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This data class represents a Todo item in the application.
 * It is annotated with @Entity, indicating that this class is an entity in the Room database.
 * Each instance of this class represents a row in the database.
 *
 * @property title The title of the Todo item. This is a non-nullable String.
 * @property description The description of the Todo item. This is a nullable String.
 * @property isDone The completion status of the Todo item. This is a Boolean.
 * @property id The unique ID of the Todo item. This is a nullable Int and is the primary key in the database.
 */
@Entity
data class Todo(
    val title: String,
    val description: String?,
    val isDone: Boolean,
    @PrimaryKey val id: Int? = null
)