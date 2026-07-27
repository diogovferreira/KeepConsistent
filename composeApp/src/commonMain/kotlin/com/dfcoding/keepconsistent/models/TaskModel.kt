package com.dfcoding.keepconsistent.models

import kotlinx.datetime.DateTimePeriod

data class TaskModel(
    val name: String,
    val description: String,
    val type: Type,
    val frequency: Frequency,
    val periodoOfTime: DateTimePeriod,
    val duration: DateTimePeriod,
    val categoryModel: CategoryModel
)


enum class Type {
    Reminder,
    Activity
}

enum class Frequency {
    Daily,
    Weekly,
    Monthly,
    Custom
}

