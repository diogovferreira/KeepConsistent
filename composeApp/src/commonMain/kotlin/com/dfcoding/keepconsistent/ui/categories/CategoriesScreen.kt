package com.dfcoding.keepconsistent.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.dfcoding.keepconsistent.models.CategoriesType
import com.dfcoding.keepconsistent.models.TaskModel
import com.dfcoding.keepconsistent.ui.components.InfoDisplayComponent
import com.dfcoding.keepconsistent.ui.components.LoadingComponent
import com.dfcoding.keepconsistent.ui.components.TaskComponent
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.ic_empty_data
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class CategoriesScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<CategoriesScreenViewModel>()
        val state by viewModel.state.collectAsState()

        CategoriesScreenStateless(
            state = state,
            onRetry = { viewModel.loadTasks() },
            onDeleteTask = { task -> viewModel.deleteTask(task) }
        )
    }
}

@Composable
fun CategoriesScreenStateless(
    state: CategoriesScreenState,
    onRetry: () -> Unit = {},
    onDeleteTask: (TaskModel) -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(top = 20.dp, start = 5.dp, end = 5.dp)
    ) {
        when (state) {
            CategoriesScreenState.Loading -> LoadingComponent()

            is CategoriesScreenState.Error -> InfoDisplayComponent(
                topText = "Something went wrong",
                bottomText = state.message,
                buttonText = "Try again",
                buttonAction = onRetry,
                isError = true
            )

            is CategoriesScreenState.Success -> {
                if (state.tasksByCategory.isEmpty()) {
                    EmptyCategoriesState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Categories",
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        state.tasksByCategory.forEach { (category, tasks) ->
                            item {
                                CategorySectionHeader(category, tasks.size)
                            }
                            items(tasks) { task ->
                                TaskComponent(
                                    task,
                                    onDelete = { onDeleteTask(task) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategorySectionHeader(category: CategoriesType, taskCount: Int) {
    Text(
        text = category.name,
        fontFamily = PoppinsFontFamily(),
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
    )
}

@Composable
private fun EmptyCategoriesState() {
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

@Preview
@Composable
fun CategoriesScreenStatelessPreview() {
    KeepConsistentTheme {

    }
}