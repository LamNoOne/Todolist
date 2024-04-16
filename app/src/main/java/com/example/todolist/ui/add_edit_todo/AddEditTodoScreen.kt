package com.example.todolist.ui.add_edit_todo

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.R
import com.example.todolist.util.UiEvent
import com.example.todolist.util.convertToDateTime
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun AddEditTodoScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditTodoViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
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
            .fillMaxSize(),
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
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
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
            TimeManager(context = context, viewModel)
            Spacer(modifier = Modifier.height(8.dp))
            TodoStatusDropdown(viewModel)
        }
    }
}

@Composable
fun TodoStatusDropdown(viewModel: AddEditTodoViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Completed", "Incomplete")
    var selectedIndex by mutableStateOf(if (viewModel.isDone) 0 else 1)

    Column(modifier = Modifier.fillMaxWidth()) {

        Button(
            onClick = { expanded = !expanded },
            colors = ButtonDefaults.buttonColors(Color.White)
        ) {
            Text(text = options[selectedIndex], color = Color.Black, fontSize = 16.sp)
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
            title = {
                Text(
                    text = "Confirm Exit",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            text = { Text("You have unsaved changes. Are you sure you want to exit?") },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(
                        text = "Cancel",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.primaryVariant
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onPopBackStack()
                }) {
                    Text(
                        text = "Exit",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.primaryVariant
                    )
                }
            }
        )
    }
}

@Composable
fun TimeManager(context: Context, viewModel: AddEditTodoViewModel) {

    val timestamp by viewModel.timestamp.collectAsState()

    val (localDateCurrent, localTimeCurrent) = convertToDateTime(timestamp)

    var pickedDate by remember { mutableStateOf(localDateCurrent) }
    var pickedTime by remember { mutableStateOf(localTimeCurrent) }

    LaunchedEffect(timestamp) {
        val (newLocalDate, newLocalTime) = convertToDateTime(timestamp)
        pickedDate = newLocalDate
        pickedTime = newLocalTime
    }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { dateDialogState.show() },
                colors = ButtonDefaults.buttonColors(Color.White)
            ) {
                Text(text = "Pick Date/Time: $pickedDate $pickedTime", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }

    MaterialDialog(
        dialogState = dateDialogState,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        buttons = {
            positiveButton("Ok") {
                viewModel.onEvent(AddEditTodoEvent.OnTimestampChange("$pickedDate $pickedTime"))
                Toast.makeText(
                    context,
                    "Selected date: $pickedDate $pickedTime",
                    Toast.LENGTH_SHORT
                ).show()
            }
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            initialDate = localDateCurrent,
            title = "Pick a date",
            locale = Locale.US
        ) {
            pickedDate = it
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(
                onClick = { timeDialogState.show() },
                modifier = Modifier
                    .fillMaxWidth()
                    .topAndBottomBorder(1.dp, Color.Gray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.clock),
                        contentDescription = "clock"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Pick Time", fontSize = 16.sp)
                }
            }
        }
    }

    MaterialDialog(
        dialogState = timeDialogState,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        buttons = {
            positiveButton("Ok") {
                Toast.makeText(
                    context,
                    "Selected time: $pickedTime",
                    Toast.LENGTH_SHORT
                ).show()
            }
            negativeButton(text = "Cancel")
        }
    ) {
        timepicker(
            initialTime = localTimeCurrent,
            title = "Pick a time",
            is24HourClock = true
        ) {
            pickedTime = it
        }
    }
}

private fun Int.toBoolean(): Boolean {
    return this != 0
}

fun Modifier.topAndBottomBorder(
    strokeWidth: Dp = 1.dp,
    color: Color = Color.LightGray
) = composed(factory = {
    val density = LocalDensity.current
    val strokeWidthPx = density.run { strokeWidth.toPx() }
    Modifier.drawBehind {
        val width = size.width
        val top = 0f
        val bottom = size.height - strokeWidthPx / 2f
        drawLine(
            color = color,
            start = Offset(0f, top),
            end = Offset(width, top),
            strokeWidth = strokeWidthPx
        )
        drawLine(
            color = color,
            start = Offset(0f, bottom),
            end = Offset(width, bottom),
            strokeWidth = strokeWidthPx
        )
    }
})