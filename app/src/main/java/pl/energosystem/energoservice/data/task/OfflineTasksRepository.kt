package pl.energosystem.energoservice.data.task

import kotlinx.coroutines.flow.Flow

class OfflineTasksRepository(private val taskDao: TaskDao): TasksRepository {
    override fun getAllTasksStream(): Flow<List<Task>> = taskDao.getAllTasks()

    override fun getTaskStream(id: Int): Flow<Task?> = taskDao.getTaskByID(id)

    override fun getTasksByDate(date: String): Flow<List<Task>> = taskDao.getTasksByDate(date)

    override suspend fun insertTask(task: Task) = taskDao.insert(task)

    override suspend fun deleteTask(task: Task) = taskDao.delete(task)

    override suspend fun updateTask(task: Task) = taskDao.update(task)
}