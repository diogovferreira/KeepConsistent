package com.dfcoding.keepconsistent.ui.addtask

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dfcoding.keepconsistent.models.CategoriesType
import com.dfcoding.keepconsistent.models.CategoryModel
import com.dfcoding.keepconsistent.models.Frequency
import com.dfcoding.keepconsistent.models.TaskModel
import com.dfcoding.keepconsistent.models.WeekDays
import com.dfcoding.keepconsistent.ui.components.AppTextField
import com.dfcoding.keepconsistent.ui.components.ButtonComponent
import com.dfcoding.keepconsistent.ui.components.InfoDisplayComponent
import com.dfcoding.keepconsistent.ui.components.LoadingComponent
import com.dfcoding.keepconsistent.ui.components.TimePickerDialog
import com.dfcoding.keepconsistent.util.formatPickedDate
import com.dfcoding.keepconsistent.util.formatPickedTime
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.ic_arrow_back
import kotlinx.datetime.DateTimePeriod
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class AddTaskScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<AddTaskScreenViewModel>()
        val addTaskState by viewModel.addTaskState.collectAsState()

        AddTaskScreenStateless(
            goBack = { navigator.pop() },
            addTask = { task ->
                viewModel.addTask(task)
            },
            addTaskState = addTaskState,
            onAddTaskSuccess = {
                viewModel.resetState()
                navigator.pop()
            },
            resetState = { viewModel.resetState() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreenStateless(
    goBack: () -> Unit = {},
    addTask: (TaskModel) -> Unit = {},
    addTaskState: AddTaskState,
    onAddTaskSuccess: () -> Unit = {},
    resetState: () -> Unit = {}
) {

    val frequencyList = Frequency.entries.toMutableList()
    val categoryList = CategoriesType.entries.toMutableList()
    val weekDaysList = WeekDays.entries.toMutableList()

    var selectedFrequency by remember { mutableStateOf(frequencyList[0]) }
    var selectedWeekDays by remember { mutableStateOf(setOf<WeekDays>()) }
    var selectedMonthDays by remember { mutableStateOf(setOf<Int>()) }
    var customDate by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categoryList[0]) }
    var taskName by remember { mutableStateOf("") }
    var taskDetails by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }

    val dateText = selectedDateMillis?.let { formatPickedDate(it) } ?: ""
    val timeText = formatPickedTime(selectedHour, selectedMinute)

    val isFormValid = taskName.isNotBlank() && when (selectedFrequency) {
        Frequency.Custom -> selectedDateMillis != null
        Frequency.Weekly -> selectedWeekDays.isNotEmpty()
        Frequency.Monthly -> selectedMonthDays.isNotEmpty()
        else -> true
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainer)
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(all = 20.dp)) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    modifier = Modifier.align(Alignment.CenterStart).clickable { goBack() },
                )
                Text(
                    "Add Task",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            AppTextField(
                taskName,
                onValueChange = { taskName = it },
                isPassword = false,
                placeholder = "Task name",
                label = ""
            )

            AppTextField(
                taskDetails,
                onValueChange = { taskDetails = it },
                isPassword = false,
                placeholder = "Task details (Optional)",
                label = ""
            )

            SelectorRow(
                title = "Frequency",
                options = frequencyList,
                selectedOption = selectedFrequency,
                onOptionSelected = { selectedFrequency = it },
                optionLabel = { it.name }
            )

            when (selectedFrequency) {
                Frequency.Custom -> {
                    AppTextField(
                        dateText, onValueChange = {}, isPassword = false,
                        placeholder = "Date", label = "Date", isCalendar = true,
                        onIconClick = { showDatePicker = true },
                        readOnly = true
                    )
                }

                Frequency.Weekly -> {
                    MultiSelectorRow(
                        title = "Days of the week",
                        options = weekDaysList,
                        selectedOptions = selectedWeekDays,
                        onOptionToggled = { day ->
                            selectedWeekDays = if (day in selectedWeekDays) {
                                selectedWeekDays - day
                            } else {
                                selectedWeekDays + day
                            }
                        },
                        optionLabel = { it.name }
                    )
                }

                Frequency.Monthly -> {
                    MonthDaysSelector(
                        title = "Days of the month",
                        selectedDays = selectedMonthDays,
                        onDayToggled = { day ->
                            selectedMonthDays = if (day in selectedMonthDays) {
                                selectedMonthDays - day
                            } else {
                                selectedMonthDays + day
                            }
                        }
                    )
                }

                else -> {}
            }

            SelectorRow(
                title = "Category",
                options = categoryList,
                selectedOption = selectedCategory,
                onOptionSelected = { selectedCategory = it },
                optionLabel = { it.name }
            )

            AppTextField(
                timeText,
                onValueChange = {},
                isPassword = false,
                placeholder = "Time",
                label = "Time",
                isTimePicker = true,
                onIconClick = { showTimePicker = true },
                readOnly = true
            )
        }

        ButtonComponent(
            text = "Add Task",
            onClick = {
                if (isFormValid) {
                    addTask(
                        TaskModel(
                            name = taskName,
                            description = taskDetails,
                            frequency = selectedFrequency,
                            customDate = if (selectedFrequency == Frequency.Custom) {
                                selectedDateMillis
                            } else null,
                            listOfWeekDays = if (selectedFrequency == Frequency.Weekly) {
                                selectedWeekDays.map { it.name }
                            } else null,
                            listOfMonthDays = if (selectedFrequency == Frequency.Monthly) {
                                selectedMonthDays.toList()
                            } else null,
                            timeOfDay = DateTimePeriod(
                                hours = selectedHour,
                                minutes = selectedMinute
                            ),
                            duration = null,
                            categoryModel = CategoryModel(selectedCategory)
                        )
                    )
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        )
    }

    if (showDatePicker) {
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateMillis = datePickerState.selectedDateMillis
                    showDatePicker = false
                    customDate = formatPickedDate(selectedDateMillis!!)
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            is24Hour = true
        )
        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = {
                selectedHour = timePickerState.hour
                selectedMinute = timePickerState.minute
                showTimePicker = false
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }

    when (addTaskState) {
        is AddTaskState.Error -> {
            InfoDisplayComponent(
                topText = "Something went wrong",
                bottomText = addTaskState.message,
                buttonText = "Try again",
                buttonAction = { resetState() },
                isError = true
            )
        }

        AddTaskState.Idle -> {}
        AddTaskState.Loading -> LoadingComponent()
        AddTaskState.Success -> {
            onAddTaskSuccess()
        }
    }
}

@Composable
fun SelectorOption(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.surface
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            fontSize = 14.sp,
            fontFamily = PoppinsFontFamily(),
            color = contentColor
        )
    }
}

@Composable
fun <T> SelectorRow(
    title: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    optionLabel: (T) -> String = { it.toString() }
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = PoppinsFontFamily(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(options) { option ->
                SelectorOption(
                    text = optionLabel(option),
                    isSelected = option == selectedOption,
                    onClick = { onOptionSelected(option) }
                )
            }
        }
    }
}

// Like SelectorRow, but any number of options can be active at once — used for
// "days of the week" where a weekly task can repeat on several days.
@Composable
fun <T> MultiSelectorRow(
    title: String,
    options: List<T>,
    selectedOptions: Set<T>,
    onOptionToggled: (T) -> Unit,
    optionLabel: (T) -> String = { it.toString() }
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = PoppinsFontFamily(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(options) { option ->
                SelectorOption(
                    text = optionLabel(option),
                    isSelected = option in selectedOptions,
                    onClick = { onOptionToggled(option) }
                )
            }
        }
    }
}

// Grid of 1-31 for picking which day(s) of the month a monthly task repeats on.
@Composable
fun MonthDaysSelector(
    title: String,
    selectedDays: Set<Int>,
    onDayToggled: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = PoppinsFontFamily(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth().height(180.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items((1..31).toList()) { day ->
                SelectorOption(
                    text = day.toString(),
                    isSelected = day in selectedDays,
                    onClick = { onDayToggled(day) }
                )
            }
        }
    }
}

@Preview
@Composable
fun AddTaskScreenStatelessPreview() {
    KeepConsistentTheme {
        AddTaskScreenStateless(
            goBack = TODO(),
            addTask = TODO(),
            addTaskState = TODO(),
            onAddTaskSuccess = TODO(),
            resetState = TODO()
        )
    }
}