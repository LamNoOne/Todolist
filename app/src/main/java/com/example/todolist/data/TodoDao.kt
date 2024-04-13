package com.example.todolist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    /**
     * Inserts a new todo into the database.
     * If the todo already exists, replace it.
     *
     * @param todo The todo to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    /**
     * Deletes a todo from the database.
     *
     * @param toto The todo to be deleted.
     */
    @Delete
    suspend fun deleteTodo(toto: Todo)

    /**
     * Retrieves a todo by its id.
     *
     * @param id The id of the todo.
     * @return The todo with the given id, or null if no todo with that id was found.
     */
    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun getTodoById(id: Int): Todo?

    /**
     * Retrieves all todos from the database.
     *
     * @return A list of all todos in the database.
     */
    @Query("SELECT * FROM todo")
    fun getTodos(): Flow<List<Todo>>
}