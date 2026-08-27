package com.dfcoding.keepconsistent.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.dfcoding.keepconsistent.ui.addtask.AddTaskScreen
import com.dfcoding.keepconsistent.ui.categories.CategoriesScreen
import com.dfcoding.keepconsistent.ui.components.ButtonComponent
import com.dfcoding.keepconsistent.ui.components.CategoryItem
import com.dfcoding.keepconsistent.ui.components.DaySelector
import com.dfcoding.keepconsistent.ui.components.InfoDisplayComponent
import com.dfcoding.keepconsistent.ui.components.LoadingComponent
import com.dfcoding.keepconsistent.ui.components.TaskComponent
import com.dfcoding.keepconsistent.ui.components.dateRange
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.ic_empty_data
import keepconsistent.composeapp.generated.resources.ic_megaphone
import keepconsistent.composeapp.generated.resources.ic_pen
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenViewModel>()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            viewModel.refresh()
        }

        when (val currentState = state) {
            HomeScreenState.Loading -> LoadingComponent()

            is HomeScreenState.Error -> InfoDisplayComponent(
                topText = "Something went wrong",
                bottomText = currentState.message,
                buttonText = "Try again",
                buttonAction = { viewModel.refresh() },
                isError = true
            )

            is HomeScreenState.Success -> {
                HomeScreenStateless(
                    tasks = currentState.tasks,
                    categories = currentState.categories,
                    selectedDate = currentState.selectedDate,
                    onDateSelected = { viewModel.selectDate(it) },
                    isTaskCompleted = { currentState.completedTaskIds.contains(it.id) },
                    onToggleComplete = { viewModel.toggleComplete(it) },
                    onDeleteTask = { viewModel.deleteTask(it) },
                    addTask = { navigator.push(AddTaskScreen()) },
                    onCategoryClick = {navigator.push(CategoriesScreen())}
                )
            }
        }
    }
}


@Composable
fun HomeScreenStateless(
    tasks: List<TaskModel>,
    categories: List<CategoryModel>,
    selectedDate: LocalDate = dateRange().first(),
    onDateSelected: (LocalDate) -> Unit = {},
    isTaskCompleted: (TaskModel) -> Boolean = { false },
    onToggleComplete: (TaskModel) -> Unit = {},
    onDeleteTask: (TaskModel) -> Unit = {},
    addTask: () -> Unit = {},
    onCategoryClick: () -> Unit = {}
) {
    val categoriesListState = rememberLazyListState()
    val hasTasks = tasks.isNotEmpty()

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainer).padding(top = 20.dp, start = 5.dp, end = 5.dp)
            .navigationBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { GreetingsHeader(tasks) }

            item {
                DaySelector(
                    days = dateRange(),
                    selectedDate = selectedDate,
                    onDateSelected = onDateSelected
                )
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(
                        text = "Categories",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        modifier = Modifier.clickable{onCategoryClick()},
                        text = "See all",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            }

            item {
                LazyRow(
                    state = categoriesListState,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category.category.name,
                            icon = category.category.icon,
                            tasks = category.tasks,
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Daily Tasks",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (hasTasks) {

                items(tasks) { task ->
                    TaskComponent(
                        task = task,
                        isCompleted = isTaskCompleted(task),
                        onToggleComplete = { onToggleComplete(task) },
                        onDelete = { onDeleteTask(task) }
                    )
                }
            } else {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_empty_data),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "No tasks for today!",
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

        }

        ButtonComponent(
            text = "Add Task",
            icon = Res.drawable.ic_pen,
            onClick = { addTask() },
            modifier = Modifier.align(Alignment.BottomEnd).padding(all = 20.dp)
        )
    }
}


@Composable
fun GreetingsHeader(tasks: List<TaskModel>) {

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Hello 👋",
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_megaphone),
                    contentDescription = "Megaphone",
                    tint = MaterialTheme.colorScheme.error
                )

                Text(
                    "${tasks.size} tasks",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
                Text(
                    " are waiting for you today",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 12.sp
                )
            }
        }
    }

}

@Preview
@Composable
fun HomeScreenStatelessPreview() {
    KeepConsistentTheme {
        HomeScreenStateless(
            tasks = listOf(
                TaskModel(
                    name = "Learn Piano",
                    description = "Description",
                    frequency = Frequency.Daily,
                    customDate = null,
                    timeOfDay = DateTimePeriod(hours = 8),
                    duration = DateTimePeriod(minutes = 30),
                    categoryType = CategoriesType.Personal,
                    listOfWeekDays = null,
                    listOfMonthDays = null
                ),
                TaskModel(
                    name = "Program",
                    description = "Description",
                    frequency = Frequency.Daily,
                    customDate = null,
                    timeOfDay = DateTimePeriod(hours = 8),
                    duration = DateTimePeriod(minutes = 30),
                    categoryType = CategoriesType.Work,
                    listOfWeekDays = null,
                    listOfMonthDays = null
                ),
            ),
            categories = listOf()
        )
    }
}