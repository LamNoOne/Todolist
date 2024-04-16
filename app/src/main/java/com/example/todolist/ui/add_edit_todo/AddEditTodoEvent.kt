package com.example.todolist.ui.add_edit_todo

sealed class AddEditTodoEvent {
    data class OnTitleChange(val title: String): AddEditTodoEvent()
    data class OnDescriptionChange(val description: String): AddEditTodoEvent()
    data class OnTimestampChange(val timestamp: String): AddEditTodoEvent()
    data class OnStatusChange(val isDone: Boolean): AddEditTodoEvent()
    object OnSaveTodoClick: AddEditTodoEvent()
}