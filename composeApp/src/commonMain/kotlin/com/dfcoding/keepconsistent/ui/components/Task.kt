package com.dfcoding.keepconsistent.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.dfcoding.keepconsistent.util.formatPickedTime
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.ic_fire
import kotlinx.datetime.DateTimePeriod
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TaskComponent(
    task: TaskModel,
    isCompleted: Boolean = false,
    onToggleComplete: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Column(
        modifier = Modifier.wrapContentWidth().clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)

    ) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(
                    text = task.name,
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = task.categoryModel.category.name,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Column {
                Text(
                    text = "Time",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                )
                Text(
                    text = formatPickedTime(task.timeOfDay.hours, task.timeOfDay.minutes),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Column {
                Text(
                    text = "Periodicy",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                )
                Text(
                    text = task.frequency.name,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                StreakComponent(streak = task.currentStreak)
                Checkbox(checked = isCompleted, onCheckedChange = { onToggleComplete() })
            }
        }

        Text(
            text = "Delete",
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 12.dp).clickable { onDelete() }
        )
    }
}


@Composable
fun StreakComponent(streak: Int) {
    Row(
        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_fire),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )

        Text(
            "$streak",
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}


@Preview
@Composable
fun TaskComponentPreview() {
    KeepConsistentTheme {
        TaskComponent(
            task = TaskModel(
                name = "Learn Piano",
                description = "Description",
                frequency = Frequency.Daily,
                duration = DateTimePeriod(minutes = 30),
                categoryModel = CategoryModel(CategoriesType.Personal),
                customDate = null,
                timeOfDay = DateTimePeriod(hours = 8),
                listOfWeekDays = null,
                listOfMonthDays = null,
                currentStreak = 12
            ),
            isCompleted = false,
            onToggleComplete = {},
            onDelete = {}
        )
    }
}