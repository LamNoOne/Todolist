package com.example.todolist.data.remote

import androidx.room.*
import com.example.todolist.domain.model.Todo
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the todos table.
 * This class represents the operations that can be performed on the Todo table.
 */
@Dao
interface TodoDao {

    /**
     * Insert a todo in the database. If the todo already exists, replace it.
     * @param todo the todo to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    /**
     * Delete a todo from the database
     * @param todo the todo to be deleted.
     */
    @Delete
    suspend fun deleteTodo(todo: Todo)

    /**
     * Get a todo by id.
     * @param id the id of the todo.
     * @return the todo from the database.
     */
    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun getTodoById(id: Int): Todo?

    /**
     * Get all todos.
     * @return all todos from the database.
     */
    @Query("SELECT * FROM todo")
    fun getTodos(): Flow<List<Todo>>
}