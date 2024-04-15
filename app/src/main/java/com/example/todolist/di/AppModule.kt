package com.example.todolist.di

import android.app.Application
import androidx.room.Room
import com.example.todolist.data.TodoDatabase
import com.example.todolist.domain.repository.TodoRepository
import com.example.todolist.data.repository.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module for dependency injection.
 * This class provides the necessary dependencies for the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the TodoDatabase instance.
     * This method creates a new instance of the TodoDatabase.
     * @param app the application context.
     * @return the TodoDatabase instance.
     */
    @Provides
    @Singleton
    fun provideTodoDatabase(app: Application): TodoDatabase {
        return Room.databaseBuilder(
            app,
            TodoDatabase::class.java,
            "todo_db"
        ).build()
    }

    /**
     * Provides the TodoRepository instance.
     * This method creates a new instance of the TodoRepository.
     * @param db the TodoDatabase instance.
     * @return the TodoRepository instance.
     */
    @Provides
    @Singleton
    fun provideTodoRepository(db: TodoDatabase): TodoRepository {
        return TodoRepositoryImpl(db.dao)
    }
}