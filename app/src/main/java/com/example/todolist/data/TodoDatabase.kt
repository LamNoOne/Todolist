package com.example.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.data.remote.TodoDao
import com.example.todolist.domain.model.Todo

@Database(
    entities = [Todo::class],
    version = 1,
    exportSchema = true
)
abstract class TodoDatabase: RoomDatabase() {

    abstract val dao: TodoDao
}