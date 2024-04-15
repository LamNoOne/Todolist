package com.example.todolist.ui.add_edit_todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.model.Todo
import com.example.todolist.domain.repository.TodoRepository
import com.example.todolist.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the AddEditTodo screen.
 * This class handles the logic for adding and editing todos.
 */
@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    /**
     * The todo to be edited. Null if a new todo is being created.
     */
    var todo by mutableStateOf<Todo?>(null)
        private set

    /**
     * The title of the todo.
     */
    var title by mutableStateOf("")
        private set

    /**
     * The description of the todo.
     */
    var description by mutableStateOf("")
        private set

    /**
     * The status of the todo.
     */
    var isDone by mutableStateOf<Boolean>(false)
        private set

    /**
     * The status of saving the todo.
     */
    private var isSaving by mutableStateOf(true)
    // Get immutable version of isSaving
    val _isSaving get() = isSaving

    /**
     * Channel for UI events.
     */
    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    /**
     * Initialize the ViewModel.
     * If a todoId is provided, load the todo from the repository.
     */
    init {
        val todoId = savedStateHandle.get<Int>("todoId")!!
        if(todoId != -1) {
            viewModelScope.launch {
                repository.getTodoById(todoId)?.let { todo ->
                    title = todo.title
                    description = todo.description ?: ""
                    isDone = todo.isDone
                    this@AddEditTodoViewModel.todo = todo
                }
            }
        }
    }

    /**
     * Handle events from the UI.
     * @param event the event to handle.
     */
    fun onEvent(event: AddEditTodoEvent) {
        when(event) {
            is AddEditTodoEvent.OnTitleChange -> {
                title = event.title
            }
            is AddEditTodoEvent.OnDescriptionChange -> {
                description = event.description
            }
            is AddEditTodoEvent.OnStatusChange -> {
                isDone = event.isDone
                if(title.isNotBlank() && todo?.id != null) {
                    viewModelScope.launch {
                        repository.insertTodo(
                            Todo(
                                title = title,
                                description = description,
                                isDone = isDone,
                                id = todo?.id
                            )
                        )
                    }
                }
            }
            is AddEditTodoEvent.OnSaveTodoClick -> {
                viewModelScope.launch {
                    if(title.isBlank()) {
                        sendUiEvent(UiEvent.ShowSnackBar(
                            message = "The title can't be empty"
                        ))
                        return@launch
                    }
                    repository.insertTodo(
                        Todo(
                            title = title,
                            description = description,
                            isDone = isDone,
                            id = todo?.id
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
        }
        isSaving = event is AddEditTodoEvent.OnSaveTodoClick || event is AddEditTodoEvent.OnStatusChange
    }

    /**
     * Send a UI event.
     * @param event the event to send.
     */
    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}