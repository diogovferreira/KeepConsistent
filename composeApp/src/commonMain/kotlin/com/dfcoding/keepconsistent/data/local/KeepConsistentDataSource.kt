package com.dfcoding.keepconsistent.data.local

import com.dfcoding.keepconsistent.database.ConsistentDatabase
import com.dfcoding.keepconsistent.database.Task
import com.dfcoding.keepconsistent.models.CategoriesType
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
        category = task.categoryType.name,
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

    // Whether a task was marked complete for a specific calendar day (LocalDate.toEpochDays()),
    // not just "was it completed most recently" — lets a user mark a day other than today.
    fun isCompletedForDay(taskId: Long, epochDay: Int): Boolean =
        queries.isCompletedForDay(taskId, epochDay.toLong()).executeAsOne()

    // Logs a completion for the given day (no-op if already logged, via INSERT OR IGNORE) and
    // recomputes the cached streak on Task in the same transaction — see
    // docs/notification-streak-design.md for the algorithm this mirrors.
    fun setCompleted(
        taskId: Long,
        epochDay: Int,
        completed: Boolean,
        completedAtMillis: Long,
        isDueOn: (Int) -> Boolean
    ) {
        queries.transaction {
            if (completed) {
                queries.insertCompletion(taskId, epochDay.toLong(), completedAtMillis)
            } else {
                queries.deleteCompletion(taskId, epochDay.toLong())
            }

            val days = queries.selectCompletionDays(taskId).executeAsList().map { it.toInt() }.toSet()
            val (current, longest) = computeStreaks(days, isDueOn)

            queries.updateStreak(
                currentStreak = current.toLong(),
                longestStreak = longest.toLong(),
                epochDay = days.maxOrNull()?.toLong(),   // nullable now — undoing the only completion clears it
                id = taskId
            )
        }
    }


    /** Returns current streak to longest streak, walking due days backwards from the last completion. */
    fun computeStreaks(completedDays: Set<Int>, isDueOn: (Int) -> Boolean): Pair<Int, Int> {
        if (completedDays.isEmpty()) return 0 to 0

        var run = 0
        var longest = 0
        var current = -1

        for (day in completedDays.max() downTo completedDays.min()) {
            if (!isDueOn(day)) continue
            if (day in completedDays) {
                run++
                longest = maxOf(longest, run)
            } else {
                if (current == -1) current = run   // first gap from the top ends the live streak
                run = 0
            }
        }
        return (if (current == -1) run else current) to longest
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
        categoryType = CategoriesType.valueOf(category),
        currentStreak = currentStreak.toInt(),
        longestStreak = longestStreak.toInt(),
        lastCompletedEpochDay = lastCompletedEpochDay?.toInt()
    )

}