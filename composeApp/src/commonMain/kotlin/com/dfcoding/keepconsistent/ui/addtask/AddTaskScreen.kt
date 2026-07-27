package com.dfcoding.keepconsistent.ui.addtask

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dfcoding.keepconsistent.models.CategoriesType
import com.dfcoding.keepconsistent.models.Frequency
import com.dfcoding.keepconsistent.models.Type
import com.dfcoding.keepconsistent.ui.components.AppTextField
import com.dfcoding.keepconsistent.ui.components.ButtonComponent
import com.dfcoding.keepconsistent.ui.components.TimePickerDialog
import com.dfcoding.keepconsistent.util.formatPickedDate
import com.dfcoding.keepconsistent.util.formatPickedTime
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class AddTaskScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        AddTaskScreenStateless(
            goBack = {navigator.pop()},
            addTask = {}
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreenStateless(goBack : () -> Unit = {}, addTask: () -> Unit = {}) {

    val typeList = Type.entries.toMutableList()
    val frequencyList = Frequency.entries.toMutableList()
    val categoryList = CategoriesType.entries.toMutableList()

    var selectedType by remember { mutableStateOf(typeList[0]) }
    var selectedFrequency by remember { mutableStateOf(frequencyList[0]) }
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

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainer)
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(all = 20.dp)) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    modifier = Modifier.align(Alignment.CenterStart).clickable{goBack()},
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
                onValueChange = {taskName = it},
                isPassword = false,
                placeholder = "Task name",
                label = ""
            )

            AppTextField(
                taskDetails,
                onValueChange = {taskDetails = it},
                isPassword = false,
                placeholder = "Task details (Optional)",
                label = ""
            )

            SelectorRow(
                title = "Type",
                options = typeList,
                selectedOption = selectedType,
                onOptionSelected = { selectedType = it },
                optionLabel = { it.name }
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
            onClick = {},
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

@Preview
@Composable
fun AddTaskScreenStatelessPreview() {
    KeepConsistentTheme {
        AddTaskScreenStateless()
    }
}