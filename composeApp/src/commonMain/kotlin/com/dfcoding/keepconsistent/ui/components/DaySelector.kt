package com.dfcoding.keepconsistent.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
@Composable
fun DaySelector(
    days: List<LocalDate>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState() // creates the object that tracks lazyRow or Column scrool position, which index,scroll offset...
    val todayIndex = remember(days){
        days.indexOf(Clock.System.todayIn(TimeZone.currentSystemDefault())).coerceAtLeast(0)
    }

    LaunchedEffect(Unit){
        listState.scrollToItem(todayIndex)
    }

    val visibleMonth by remember {
        derivedStateOf {
            val index = listState.firstVisibleItemIndex.coerceIn(days.indices)
            days[index].month
        }
    }

    Column(modifier = modifier) {
        Text(
            text = visibleMonth.name.lowercase().replaceFirstChar { it.uppercase() },
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(days, key = { it.toEpochDays() }) { date ->
                DayCell(
                    date = date,
                    isSelected = date == selectedDate,
                    onClick = { onDateSelected(date) },
                    modifier = Modifier.width(64.dp)
                )
            }
        }
    }
}


@Composable
private fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background = if (isSelected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .clickable { onClick() }
            .padding(vertical = 14.dp)
    ) {
        Text(
            text = date.dayOfWeek.name.take(3)
                .lowercase().replaceFirstChar { it.uppercase() },
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = contentColor
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = date.dayOfMonth.toString(),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = contentColor
        )
    }
}


@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun DaySelectorPreview() {
    KeepConsistentTheme {
        DaySelector(
            days = dateRange(),
            selectedDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            onDateSelected = {}
        )
    }
}

@OptIn(ExperimentalTime::class)
fun dateRange(
    center: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    daysBefore: Int = 14,
    daysAfter: Int = 90
): List<LocalDate> = (-daysBefore..daysAfter).map { offset -> center.plus(offset, DateTimeUnit.DAY) }