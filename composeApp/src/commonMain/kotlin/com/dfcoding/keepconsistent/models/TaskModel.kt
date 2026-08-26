package com.dfcoding.keepconsistent.models

import kotlinx.datetime.DateTimePeriod


data class TaskModel(
    val id: Long? = null, // null for a task not yet persisted; set once read back from the DB
    val name: String,
    val description: String,
    val frequency: Frequency, // if frequency custom, weekly or monthly must have a custom date, if custom(date), if weekly(days of the week) or monthly(days of the month)
    val customDate: Long?, // if customDate
    val listOfWeekDays: List<String>?, // if weekly
    val listOfMonthDays: List<Int>?, // if monthly
    val timeOfDay: DateTimePeriod, //what time is the task MANDATORY
    val duration: DateTimePeriod?, //the duration of the task, can be null
    val categoryType: CategoriesType, // CategoryModel, Personal,Work, Break, MANDATORY
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastCompletedEpochDay: Int? = null // LocalDate.toEpochDays() of the last day this task was marked complete
)

enum class Frequency {
    Daily,
    Weekly,
    Monthly,
    Custom
}

enum class WeekDays {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday
}


