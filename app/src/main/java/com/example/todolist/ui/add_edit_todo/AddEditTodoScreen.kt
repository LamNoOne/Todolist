package com.example.todolist.ui.add_edit_todo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.R
import com.example.todolist.util.UiEvent

@Composable
fun AddEditTodoScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditTodoViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }
                else -> Unit
            }
        }
    }

    BackHandlerConfirm(viewModel, onPopBackStack)
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditTodoEvent.OnSaveTodoClick)
                },
                backgroundColor = Color.DarkGray
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save",
                    tint = Color.Yellow,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TextField(
                value = viewModel.title,
                onValueChange = {
                    viewModel.onEvent(AddEditTodoEvent.OnTitleChange(it))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.title))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = viewModel.description,
                onValueChange = {
                    viewModel.onEvent(AddEditTodoEvent.OnDescriptionChange(it))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.description))
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(8.dp))
            TodoStatusDropdown(viewModel)
        }
    }
}

@Composable
fun TodoStatusDropdown(viewModel: AddEditTodoViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Completed", "Incomplete")
    var selectedIndex by mutableStateOf(if(viewModel.isDone) 0 else 1)

    Column(modifier = Modifier.fillMaxWidth()) {

        Button(onClick = { expanded = !expanded }) {
            Text(text = options[selectedIndex], color = Color.White)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                    viewModel.onEvent(AddEditTodoEvent.OnStatusChange(isDone = !index.toBoolean()))
                }) {
                    Text(text = option)
                }
            }
        }
    }
}

@Composable
fun BackHandlerConfirm(viewModel: AddEditTodoViewModel, onPopBackStack: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    BackHandler(onBack = {
        if ((viewModel.title.isNotEmpty() && viewModel.todo?.id == null) || !viewModel._isSaving) {
            showDialog = true
        } else {
            onPopBackStack()
        }
    })

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Exit", fontSize = 16.sp, fontWeight = FontWeight.Medium) },
            text = { Text("You have unsaved changes. Are you sure you want to exit?") },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "Cancel", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colors.primaryVariant)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onPopBackStack()
                }) {
                    Text(text = "Exit", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colors.primaryVariant)
                }
            }
        )
    }
}

private fun Int.toBoolean(): Boolean {
    return this != 0
}
