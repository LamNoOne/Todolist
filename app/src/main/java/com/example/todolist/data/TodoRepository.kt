package com.example.todolist.data

import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    /**
     * Inserts a new todo into the database.
     * If the todo already exists, replace it.
     *
     * @param todo The todo to be inserted.
     */
    suspend fun insertTodo(todo: Todo)

    /**
     * Deletes a todo from the database.
     *
     * @param toto The todo to be deleted.
     */
    suspend fun deleteTodo(toto: Todo)

    /**
     * Retrieves a todo by its id.
     *
     * @param id The id of the todo.
     * @return The todo with the given id, or null if no todo with that id was found.
     */
    suspend fun getTodoById(id: Int): Todo?

    /**
     * Retrieves all todos from the database.
     *
     * @return A list of all todos in the database.
     */
    fun getTodos(): Flow<List<Todo>>
}