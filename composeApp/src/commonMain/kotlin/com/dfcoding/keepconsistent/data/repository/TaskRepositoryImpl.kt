import com.dfcoding.keepconsistent.data.local.KeepConsistentDataSource
import com.dfcoding.keepconsistent.data.repository.TaskRepository
import com.dfcoding.keepconsistent.models.TaskModel

class TaskRepositoryImpl(
    private val dataSource: KeepConsistentDataSource
) : TaskRepository {

    override fun getAllTasks(): List<TaskModel> = dataSource.getAllTasks()

    override fun insertTask(task: TaskModel) {
        dataSource.insertTask(task)
    }

    override fun deleteTask(id: Long) {
        dataSource.deleteTask(id)
    }

    override fun completeTask(taskId: Long, epochDay: Int, completedAtMillis: Long) {
        dataSource.completeTask(taskId, epochDay, completedAtMillis)
    }
}