package com.dfcoding.keepconsistent.data.local

import com.dfcoding.keepconsistent.database.ConsistentDatabase
import com.dfcoding.keepconsistent.database.Task
import com.dfcoding.keepconsistent.models.CategoriesType
import com.dfcoding.keepconsistent.models.CategoryModel
import com.dfcoding.keepconsistent.models.Frequency
import com.dfcoding.keepconsistent.models.TaskModel
import kotlinx.datetime.DateTimePeriod

class KeepConsistentDataSource(private val database: ConsistentDatabase) {

    val queries = database.consistentQueries

    fun insertTask(task: TaskModel) = queries.insertTask(
        id = null,
        name = task.name,
        description = task.description,
        frequency = task.frequency.name,
        category = task.categoryModel.category.name,
        dateMillis = task.customDate,
        weekDays = task.listOfWeekDays?.joinToString(","),
        monthDays = task.listOfMonthDays?.joinToString(","),
        hour = task.timeOfDay.hours.toLong(),
        minute = task.timeOfDay.minutes.toLong(),
        currentStreak = 0,
        longestStreak = 0,
        lastCompletedEpochDay = null
    )

    fun deleteTask(id: Long) = queries.deleteTask(id)

    fun getAllTasks() = queries.getAllTasks().executeAsList().map { it.toTaskModel() }

    fun selectTasksByDate(dateMillis: Long) =
        queries.selectTasksByDate(dateMillis).executeAsList().map { it.toTaskModel() }

    fun selectTaskByCategories(category: String) =
        queries.selectTaskByCategorie(category).executeAsList().map { it.toTaskModel() }

    // Logs today's completion (no-op if already logged, via INSERT OR IGNORE) and
    // recomputes the cached streak on Task in the same transaction — see
    // docs/notification-streak-design.md for the algorithm this mirrors.
    fun completeTask(taskId: Long, epochDay: Int, completedAtMillis: Long) {
        queries.transaction {
            queries.insertCompletion(taskId, epochDay.toLong(), completedAtMillis)

            val task = queries.selectTaskById(taskId).executeAsOneOrNull() ?: return@transaction
            val lastCompleted = task.lastCompletedEpochDay?.toInt()

            val newStreak = when (lastCompleted) {
                epochDay -> task.currentStreak.toInt() // already completed today, no-op
                epochDay - 1 -> task.currentStreak.toInt() + 1 // consecutive day
                else -> 1 // gap, or first ever completion
            }
            val newLongest = maxOf(task.longestStreak.toInt(), newStreak)

            queries.updateStreak(
                currentStreak = newStreak.toLong(),
                longestStreak = newLongest.toLong(),
                epochDay = epochDay.toLong(),
                id = taskId
            )
        }
    }

    fun Task.toTaskModel() = TaskModel(
        id = id,
        name = name,
        description = description ?: "",
        frequency = Frequency.valueOf(frequency),
        customDate = dateMillis,
        listOfWeekDays = weekDays?.split(",")?.filter { it.isNotBlank() },
        listOfMonthDays = monthDays?.split(",")?.mapNotNull { it.toIntOrNull() },
        timeOfDay = DateTimePeriod(hours = hour.toInt(), minutes = minute.toInt()),
        duration = null, // not persisted — no column for it in the Task table yet
        categoryModel = CategoryModel(CategoriesType.valueOf(category)),
        currentStreak = currentStreak.toInt(),
        longestStreak = longestStreak.toInt(),
        lastCompletedEpochDay = lastCompletedEpochDay?.toInt()
    )

}