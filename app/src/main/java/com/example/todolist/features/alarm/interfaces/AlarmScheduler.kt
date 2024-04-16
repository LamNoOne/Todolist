package com.example.todolist.features.alarm.interfaces

import com.example.todolist.features.alarm.data.AlarmItem

interface AlarmScheduler {
    fun schedule(alarmItem: AlarmItem)
    fun cancel(alarmItem: AlarmItem)
}