package com.dfcoding.keepconsistent.data.repository

import com.dfcoding.keepconsistent.data.local.KeepConsistentDataSource
import com.dfcoding.keepconsistent.models.TaskModel

// NOTE: I don't have your actual TaskRepository/TaskRepositoryImpl — this is a
// reconstruction based on how it's used elsewhere (single<TaskRepository> {
// TaskRepositoryImpl(get()) } in Module.kt, and AddTaskScreenViewModel calling
// taskRepository.insertTask(task) as a suspend-context call). If your real file
// has more on it (auth-scoping, caching, etc.), paste it and I'll merge these
// three new members into it instead of replacing the file.
interface TaskRepository {
    fun getAllTasks(): List<TaskModel>
    fun insertTask(task: TaskModel)
    fun deleteTask(id: Long)
    fun completeTask(taskId: Long, epochDay: Int, completedAtMillis: Long)
}

