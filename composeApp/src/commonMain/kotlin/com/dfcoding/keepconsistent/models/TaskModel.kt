package com.dfcoding.keepconsistent.models

import kotlinx.datetime.DateTimePeriod

data class TaskModel(
    val name: String,
    val description: String,
    val type: TaskType,
    val frequency: FrequencyType,
    val periodoOfTime: DateTimePeriod,
    val duration: DateTimePeriod,
    val categoryModel: CategoryModel
)



enum class TaskType {
    Personal,
    Work,
    Break
}

enum class FrequencyType {
    Daily,
    Weekly,
    Monthly
}

