package com.example.todolist.ui.todo_list

import com.example.todolist.data.Todo

/**
 * This sealed class represents the different events that can occur in the TodoList.
 * Each event is represented as a data class or object that extends TodoListEvent.
 */
sealed class TodoListEvent {
    /**
     * This event represents the action of deleting a Todo.
     * @property todo The Todo to be deleted.
     */
    data class OnDeleteTodoClick(val todo: Todo): TodoListEvent()

    /**
     * This event represents the action of changing the done status of a Todo.
     * @property todo The Todo whose done status is to be changed.
     * @property isDone The new done status.
     */
    data class OnDoneChange(val todo: Todo, val isDone: Boolean): TodoListEvent()

    /**
     * This event represents the action of clicking the undo delete button.
     */
    object OnUndoDeleteClick: TodoListEvent()

    /**
     * This event represents the action of clicking on a Todo.
     * @property todo The Todo that was clicked.
     */
    data class OnTodoClick(val todo: Todo): TodoListEvent()

    /**
     * This event represents the action of clicking the add Todo button.
     */
    object OnAddTodoClick: TodoListEvent()
}