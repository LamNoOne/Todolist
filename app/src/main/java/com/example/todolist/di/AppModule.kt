package com.example.todolist.di

import android.app.Application
import androidx.room.Room
import com.example.todolist.data.TodoDatabase
import com.example.todolist.data.TodoRepository
import com.example.todolist.data.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * The Dagger module that provides the dependencies and their lifetime for the app.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Provides the TodoDatabase instance.
     *
     * @param app The application instance.
     * @return The TodoDatabase instance.
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
     *
     * @param database The TodoDatabase instance.
     * @return The TodoRepository instance.
     */
    @Provides
    @Singleton
    fun provideTodoRepository(database: TodoDatabase): TodoRepository {
        return TodoRepositoryImpl(database.dao)
    }
}