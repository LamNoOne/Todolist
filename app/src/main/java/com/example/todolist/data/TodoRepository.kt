package com.example.todolist.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for the todos.
 * This interface represents the operations that can be performed on the Todo data.
 */
interface TodoRepository {

    /**
     * Insert a todo.
     * This method is used to insert a new todo item.
     * @param todo the todo item to be inserted.
     */
    suspend fun insertTodo(todo: Todo)

    /**
     * Delete a todo.
     * This method is used to delete a specific todo item.
     * @param todo the todo item to be deleted.
     */
    suspend fun deleteTodo(todo: Todo)

    /**
     * Get a todo by id.
     * This method is used to retrieve a specific todo item by its id.
     * @param id the id of the todo item.
     * @return the todo item with the specified id.
     */
    suspend fun getTodoById(id: Int): Todo?

    /**
     * Get all todos.
     * This method is used to retrieve all todo items.
     * @return a list of all todo items.
     */
    fun getTodos(): Flow<List<Todo>>
}