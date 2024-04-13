package com.example.todolist.util
/**
 * This sealed class represents different types of UI events that can occur in the application.
 * Each type of event is represented as a different subclass of UiEvent.
 *
 * @property PopBackStack This object represents an event where the application needs to navigate back in the navigation stack.
 * @property Navigate This data class represents an event where the application needs to navigate to a new screen. It has a single property `route` of type `String` which represents the route to the new screen.
 * @property ShowSnackBar This data class represents an event where the application needs to show a snack bar message. It has two properties: `message` of type `String` which represents the message to be shown, and `action` of type `String?` which represents an optional action that can be performed from the snack bar.
 */
sealed class UiEvent {
    object PopBackStack : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackBar(
        val message: String,
        val action: String? = null
    ) : UiEvent()
}