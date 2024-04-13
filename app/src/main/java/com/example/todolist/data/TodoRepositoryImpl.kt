package com.example.todolist.data

import kotlinx.coroutines.flow.Flow

/**
 * This class is an implementation of the TodoRepository interface.
 * It uses a TodoDao object to interact with the database.
 *
 * @property dao The TodoDao object used to interact with the database.
 */
class TodoRepositoryImpl(
    private val dao: TodoDao
) : TodoRepository {

    /**
     * Inserts a new Todo into the database.
     *
     * @param todo The Todo to be inserted.
     */
    override suspend fun insertTodo(todo: Todo) {
        dao.insertTodo(todo)
    }

    /**
     * Deletes a Todo from the database.
     *
     * @param todo The Todo to be deleted.
     */
    override suspend fun deleteTodo(todo: Todo) {
        dao.deleteTodo(todo)
    }

    /**
     * Retrieves a Todo from the database by its id.
     *
     * @param id The id of the Todo to be retrieved.
     * @return The Todo with the given id, or null if no such Todo exists.
     */
    override suspend fun getTodoById(id: Int): Todo? {
        return dao.getTodoById(id)
    }

    /**
     * Retrieves all Todos from the database.
     *
     * @return A Flow that emits a list of all Todos in the database.
     */
    override fun getTodos(): Flow<List<Todo>> {
        return dao.getTodos()
    }
}