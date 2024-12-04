package com.example.studypaceapp

import android.content.Context
import android.content.SharedPreferences

object TaskManager {
    private const val PREF_NAME = "TaskPreferences"
    private const val KEY_TASKS = "Tasks"
    private const val KEY_STATUS = "TaskStatus"

    private lateinit var sharedPreferences: SharedPreferences

    private var tasks = mutableListOf<String>()
    private var taskStatus = mutableListOf<Boolean>()

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        loadTasks()
    }

    private fun loadTasks() {
        val savedTasks = sharedPreferences.getStringSet(KEY_TASKS, emptySet())
        val savedStatus = sharedPreferences.getString(KEY_STATUS, null)

        tasks = savedTasks?.toMutableList() ?: mutableListOf()
        taskStatus = savedStatus?.split(",")?.map { it == "true" }?.toMutableList() ?: mutableListOf()
    }

    fun saveTasks() {
        sharedPreferences.edit().apply {
            putStringSet(KEY_TASKS, tasks.toSet())
            putString(KEY_STATUS, taskStatus.joinToString(","))
            apply()
        }
    }

    fun addTask(task: String) {
        tasks.add(task)
        taskStatus.add(false)
        saveTasks()
    }

    fun updateTaskStatus(status: List<Boolean>) {
        taskStatus = status.toMutableList()
        saveTasks()
    }

    fun resetTasks() {
        tasks.clear()
        taskStatus.clear()
        saveTasks()
    }

    fun getTasks(): List<String> = tasks
    fun getTaskStatus(): List<Boolean> = taskStatus

    fun areAllTasksCompleted(): Boolean {
        return taskStatus.size == 8 && taskStatus.all { it } // Ensure all 8 tasks are completed
    }
}


