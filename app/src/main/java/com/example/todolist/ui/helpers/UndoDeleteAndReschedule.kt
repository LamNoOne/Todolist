package com.example.todolist.ui.helpers

import com.example.todolist.domain.model.Todo
import com.example.todolist.features.alarm.classes.AndroidAlarmScheduler
import com.example.todolist.features.alarm.data.AlarmItem
import com.example.todolist.util.getSecondsFromCurrentToCurrentTimeZone
import java.time.LocalDateTime
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
fun undoDeleteAndReschedule(todo: Todo, context: Context) {
    val seconds = todo.timestamp?.let {
        getSecondsFromCurrentToCurrentTimeZone(it)
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

    if (seconds != null && seconds != (-1).toLong() && !todo.isDone && seconds >= 0) {
        alarmItem?.let {
            scheduler.schedule(it)
            println("NOTIFICATION SCHEDULED: ${it.message}")
            println(it.time)
        }
    }
}