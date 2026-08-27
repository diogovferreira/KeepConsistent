package com.dfcoding.keepconsistent.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dfcoding.keepconsistent.models.CategoriesType
import com.dfcoding.keepconsistent.models.CategoryModel
import com.dfcoding.keepconsistent.models.Frequency
import com.dfcoding.keepconsistent.models.TaskModel
import com.dfcoding.keepconsistent.models.accentColor
import com.dfcoding.keepconsistent.util.formatPickedTime
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.ic_calendar
import keepconsistent.composeapp.generated.resources.ic_clock
import keepconsistent.composeapp.generated.resources.ic_fire
import kotlinx.datetime.DateTimePeriod
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.collections.forEach


@Composable
fun TaskComponent(
    task: TaskModel,
    isCompleted: Boolean = false,
    onToggleComplete: () -> Unit = {},
    onDelete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val accent = task.categoryType.accentColor()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        // Left category bar
        Box(
            modifier = Modifier
                .padding(vertical = 14.dp)
                .width(5.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp))
                .background(accent)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, top = 12.dp, bottom = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.name,
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = task.categoryType.name,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            Icons.Default.MoreHoriz,
                            contentDescription = "Task options",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Delete task",
                                    fontFamily = PoppinsFontFamily(),
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LabelledValue(label = "Time") {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_clock),
                            contentDescription = null,
                            tint = accent,
                            modifier = Modifier.size(16.dp)
                        )
                        ValueText(formatPickedTime(task.timeOfDay.hours, task.timeOfDay.minutes))
                    }
                }

                Spacer(Modifier.width(28.dp))

                LabelledValue(label = "Periodicity") {
                    ValueText(task.frequency.name)
                }

                Spacer(Modifier.weight(1f))

                Checkbox(checked = isCompleted, onCheckedChange = { onToggleComplete() })

                StreakBadge(streak = task.currentStreak)
            }

            task.scheduleDetail()?.let { (label, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LabelledValue(label = label) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_calendar),
                                contentDescription = null,
                                tint = accent,
                                modifier = Modifier.size(16.dp)
                            )
                            ValueText(value)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LabelledValue(label: String, value: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
        value()
    }
}

@Composable
private fun ValueText(text: String) {
    Text(
        text = text,
        fontFamily = PoppinsFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
    )
}


@Composable
fun StreakBadge(streak: Int, modifier: Modifier = Modifier) {
    val streakColor = MaterialTheme.colorScheme.error
    val isActive = streak > 0

    Column(
        modifier = modifier
            .size(58.dp)
            .clip(CircleShape)
            .background(streakColor.copy(alpha = if (isActive) 0.08f else 0.04f))
            .border(
                width = 3.dp,
                color = streakColor.copy(alpha = if (isActive) 1f else 0.2f),
                shape = CircleShape
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_fire),
            contentDescription = null,
            tint = streakColor.copy(alpha = if (isActive) 1f else 0.4f),
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "$streak",
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private data class ScheduleDetail(val label: String, val value: String)

private fun TaskModel.scheduleDetail(): ScheduleDetail? = when (frequency) {
    Frequency.Daily -> null
    Frequency.Weekly -> listOfWeekDays
        ?.takeIf { it.isNotEmpty() }
        ?.joinToString(", ") { it.take(3) }
        ?.let { ScheduleDetail("Week days", it) }

    Frequency.Monthly -> listOfMonthDays
        ?.takeIf { it.isNotEmpty() }
        ?.joinToString(", ")
        ?.let { ScheduleDetail("Month days", it) }

    Frequency.Custom -> customDate?.let { ScheduleDetail("Date", it.toString()) }
}

@Preview
@Composable
fun TaskComponentPreview() {
    KeepConsistentTheme {
        TaskComponent(
            task = TaskModel(
                name = "Learn Piano",
                description = "Description",
                frequency = Frequency.Weekly,
                duration = DateTimePeriod(minutes = 30),
                categoryType = CategoriesType.Personal,
                customDate = null,
                timeOfDay = DateTimePeriod(hours = 8),
                listOfWeekDays = listOf("Monday", "Tuesday"),
                listOfMonthDays = null,
                currentStreak = 12
            ),
            isCompleted = false,
            modifier = Modifier.padding(16.dp)
        )
    }
}