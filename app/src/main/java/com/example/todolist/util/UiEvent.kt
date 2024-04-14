package com.example.todolist.util

/**
 * Sealed class for UI events.
 * This class represents the different types of events that can occur in the UI.
 */
sealed class UiEvent {

    /**
     * Object representing a pop back stack event.
     * This event is used when the current fragment needs to be removed from the stack.
     */
    object PopBackStack: UiEvent()

    /**
     * Data class representing a navigation event.
     * This event is used when a navigation to a different screen is required.
     * @param route the route of the screen to navigate to.
     */
    data class Navigate(val route: String): UiEvent()

    /**
     * Data class representing a show snackbar event.
     * This event is used when a snackbar needs to be shown.
     * @param message the message to be shown in the snackbar.
     * @param action the action to be shown in the snackbar. Null if no action is required.
     */
    data class ShowSnackBar(
        val message: String,
        val action: String? = null
    ): UiEvent()
}