package com.example.todolist.ui.todo_list

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.R
import com.example.todolist.features.alarm.classes.AndroidAlarmScheduler
import com.example.todolist.features.alarm.data.AlarmItem
import com.example.todolist.util.UiEvent
import com.example.todolist.util.getSecondsFromCurrentToCurrentTimeZone
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodoListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel(),
    context: Context
) {
    val todos = viewModel.todos.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()
    var fabClicked by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = fabClicked) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                    }
                }
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                backgroundColor = Color.LightGray,
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.header),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(TodoListEvent.OnAddTodoClick)
                    fabClicked = !fabClicked
                },
                backgroundColor = Color.DarkGray
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.Yellow,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) {
        // Show the empty background if there are no todos
        if (todos.value.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .padding(horizontal = 64.dp)
                            .fillMaxWidth(),
                        painter = painterResource(id = R.drawable.none_background),
                        contentDescription = "No Todos Background",
                        contentScale = ContentScale.FillWidth
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.no_todos),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.no_todos_description),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(todos.value) { todo ->
                val padding = if (todo.id == todos.value.last().id) {
                    PaddingValues(top = 5.dp, start = 5.dp, end = 5.dp, bottom = 100.dp)
                } else {
                    PaddingValues(5.dp)
                }

                val seconds = todo.timestamp?.let { it1 ->
                    getSecondsFromCurrentToCurrentTimeZone(
                        it1
                    )
                }

                if (seconds != null) {
                    if(seconds != (-1).toLong() && !todo.isDone && seconds >= 0) {
                        val alarmItem = AlarmItem(
                            time = LocalDateTime.now().plusSeconds(seconds.toLong()),
                            message = todo.title
                        )
                        alarmItem.let {
                            val scheduler = AndroidAlarmScheduler(context)
                            scheduler.schedule(it)
                        }

                        Log.d("TodoListScreen", "Seconds: $seconds")
                        Log.d("TodoListScreen", "AlarmItem: ${todo.title}")
                    }
                }

                TodoItem(
                    todo = todo,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onEvent(TodoListEvent.OnTodoClick(todo))
                        }
                        .padding(padding)
                )
            }
        }
    }
}