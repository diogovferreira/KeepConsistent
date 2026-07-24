package com.dfcoding.keepconsistent.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.dfcoding.keepconsistent.models.CategoriesType
import com.dfcoding.keepconsistent.models.CategoryModel
import com.dfcoding.keepconsistent.models.TaskModel
import com.dfcoding.keepconsistent.ui.components.ButtonComponent
import com.dfcoding.keepconsistent.ui.components.CategoryItem
import com.dfcoding.keepconsistent.ui.components.DaySelector
import com.dfcoding.keepconsistent.ui.components.TaskComponent
import com.dfcoding.keepconsistent.ui.components.dateRange
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.ic_add_square
import keepconsistent.composeapp.generated.resources.ic_arrow_back
import keepconsistent.composeapp.generated.resources.ic_empty_data
import keepconsistent.composeapp.generated.resources.ic_megaphone
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeScreenStateless(name = "", tasks = listOf(), image = "", categories = listOf())
    }
}


@Composable
fun HomeScreenStateless(
    name: String,
    tasks: List<TaskModel>,
    image: String,
    categories: List<CategoryModel>
) {
    val categoriesListState = rememberLazyListState()
    var selectedDate by remember { mutableStateOf(dateRange().first()) }

    val hasTasks = tasks.isNotEmpty()

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainer)
            .navigationBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { GreetingsHeader(name, tasks) }

            item {
                DaySelector(
                    days = dateRange(),
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it }
                )
            }

            if (hasTasks) {
                item {
                    Text(
                        text = "Categories",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
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
                                tasks = 10
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
                items(tasks) { task -> TaskComponent(task) }
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
                            "No tasks yet!",
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }


        }
    }
}


@Composable
fun GreetingsHeader(name: String, tasks: List<TaskModel>) {

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Morning, $name \uD83D\uDC4B",
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

        Image(
            painter = painterResource(Res.drawable.ic_arrow_back),
            contentDescription = "User Image"
        )
    }

}


@Preview
@Composable
fun HomeScreenStatelessPreview() {
    KeepConsistentTheme {
        HomeScreenStateless(
            name = "Diogo",
            tasks = listOf(
                TaskModel(
                    name = "Learn Piano",
                    description = "Description",
                    type = com.dfcoding.keepconsistent.models.TaskType.Personal,
                    frequency = com.dfcoding.keepconsistent.models.FrequencyType.Daily,
                    periodoOfTime = kotlinx.datetime.DateTimePeriod(days = 1),
                    duration = kotlinx.datetime.DateTimePeriod(days = 1),
                    categoryModel = CategoryModel(CategoriesType.Personal)
                ),
                TaskModel(
                    name = "Learn Piano",
                    description = "Description",
                    type = com.dfcoding.keepconsistent.models.TaskType.Personal,
                    frequency = com.dfcoding.keepconsistent.models.FrequencyType.Daily,
                    periodoOfTime = kotlinx.datetime.DateTimePeriod(days = 1),
                    duration = kotlinx.datetime.DateTimePeriod(days = 1),
                    categoryModel = CategoryModel(CategoriesType.Personal)
                ),
                TaskModel(
                    name = "Learn Piano",
                    description = "Description",
                    type = com.dfcoding.keepconsistent.models.TaskType.Personal,
                    frequency = com.dfcoding.keepconsistent.models.FrequencyType.Daily,
                    periodoOfTime = kotlinx.datetime.DateTimePeriod(days = 1),
                    duration = kotlinx.datetime.DateTimePeriod(days = 1),
                    categoryModel = CategoryModel(CategoriesType.Personal)
                )
            ),
            image = "",
            categories = listOf(
                CategoryModel(CategoriesType.Personal),
                CategoryModel(CategoriesType.Work)
            )
        )
    }
}