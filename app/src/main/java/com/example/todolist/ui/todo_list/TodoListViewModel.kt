package com.example.todolist.ui.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.Todo
import com.example.todolist.data.TodoRepository
import com.example.todolist.util.Routes
import com.example.todolist.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the TodoList screen.
 * This ViewModel is responsible for handling user interactions and updating the UI state.
 * It uses Hilt for dependency injection.
 *
 * @property repository The repository to manage the data operations.
 */
@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {
    // LiveData to hold the list of todos.
    val todos = repository.getTodos()

    // Private channel to handle UI events.
    private val _uiEvent = Channel<UiEvent>()

    // Public flow to expose UI events to the view.
    val uiEvent = _uiEvent.receiveAsFlow()

    // Variable to hold the deleted todo for undo operation.
    private var deletedTodo: Todo? = null

    /**
     * Handles the different events triggered in the UI.
     *
     * @param event The event triggered in the UI.
     */
    fun onEvent(event: TodoListEvent) {
        when (event) {
            // Navigate to the edit screen when a todo is clicked.
            is TodoListEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate("${Routes.ADD_EDIT_TODO}?todoId=${event.todo.id}"))
            }

            // Navigate to the add screen when the add button is clicked.
            is TodoListEvent.OnAddTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }

            // Delete a todo when the delete button is clicked.
            is TodoListEvent.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    deletedTodo = event.todo
                    repository.deleteTodo(event.todo)
                    sendUiEvent(UiEvent.ShowSnackBar(
                        message = "Todo deleted",
                        action = "Undo"
                    ))
                }
            }

            // Undo the delete operation when the undo button is clicked.
            is TodoListEvent.OnUndoDeleteClick -> {
                deletedTodo?.let {
                    viewModelScope.launch {
                        repository.insertTodo(it)
                    }
                }
            }

            // Update the done status of a todo when the done checkbox is clicked.
            is TodoListEvent.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertTodo(
                        event.todo.copy(isDone = event.isDone)
                    )
                }
            }
        }
    }

    /**
     * Sends a UI event to the channel.
     *
     * @param event The event to be sent.
     */
    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}