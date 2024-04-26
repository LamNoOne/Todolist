@file:Suppress("NAME_SHADOWING")

package com.example.todolist.ui.todo_list

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.domain.model.Todo
import com.example.todolist.features.alarm.classes.AndroidAlarmScheduler
import com.example.todolist.features.alarm.data.AlarmItem
import com.example.todolist.util.Const
import com.example.todolist.util.convertFormat
import com.example.todolist.util.getSecondsFromCurrentToCurrentTimeZone
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodoItem(
    todo: Todo,
    onEvent: (TodoListEvent) -> Unit,
    modifier: Modifier = Modifier,
    context: Context
) {
    val seconds = todo.timestamp?.let { it1 ->
        getSecondsFromCurrentToCurrentTimeZone(
            it1
        )
    }

    val scheduler = AndroidAlarmScheduler(context)

    val alarmItem = seconds?.let { LocalDateTime.now().plusSeconds(it.toLong()) }?.let {
        todo.id?.let { it1 ->
            AlarmItem(
                time = it,
                message = todo.title,
                id = it1
            )
        }
    }

    if (seconds != null) {
        if (seconds != (-1).toLong() && !todo.isDone && seconds >= 0) {
            alarmItem?.let {
                scheduler.schedule(it)
            }

            Log.d("TodoListScreen", "Seconds: $seconds")
            Log.d("TodoListScreen", "AlarmItem: ${todo.title}")
        }
    }

    val todoDate =
        todo.timestamp?.let { convertFormat(it, Const.currentFormat, Const.desiredFormat) }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = todo.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {
                        alarmItem?.let {
                            scheduler.cancel(it)
                            println("NOTIFICATION CANCELLED: ${it.message}")
                            println(it.time)
                        }
                        onEvent(TodoListEvent.OnDeleteTodoClick(todo))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                todoDate?.let { Text(text = it, fontSize = 12.sp, color = Color.Black) }
                todo.description?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it, color = Color.DarkGray)
                }
            }
            Checkbox(
                checked = todo.isDone,
                onCheckedChange = { isChecked ->
                    onEvent(TodoListEvent.OnDoneChange(todo, isChecked))
                    if (isChecked) {
                        alarmItem?.let {
                            scheduler.cancel(it)
                            println("NOTIFICATION CANCELLED: ${it.message}")
                            println(it.time)
                        }
                    } else {
                        alarmItem?.let {
                            scheduler.schedule(it)
                            println("NOTIFICATION SCHEDULED: ${it.message}")
                            println(it.time)
                        }
                    }
                },
                modifier = Modifier.align(Alignment.Top)
            )
        }
    }
}