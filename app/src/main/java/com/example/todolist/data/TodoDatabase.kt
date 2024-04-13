package com.example.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Todo::class],
    version = 1
)

/**
 * A Room database that stores the list of todos.
 */
abstract class TodoDatabase: RoomDatabase() {
    /**
     * Returns the TodoDao object that will be used to interact with the database.
     */
    abstract val dao: TodoDao
}