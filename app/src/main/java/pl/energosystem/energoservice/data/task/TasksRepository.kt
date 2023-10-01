package pl.energosystem.energoservice.data.task

import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    /**
     * Retrieve all the Tasks from the the given data source.
     */
    fun getAllTasksStream(): Flow<List<Task>>

    /**
     * Retrieve an Task from the given data source that matches with the [id].
     */
    fun getTaskStream(id: Int): Flow<Task?>

    /**
     * Retrieve all Tasks from the given data source that matches with the [date]
     */
    fun getTasksByDate(date: String): Flow<List<Task>>

    /**
     * Insert Task in the data source
     */
    suspend fun insertTask(task: Task)

    /**
     * Delete Task from the data source
     */
    suspend fun deleteTask(task: Task)

    /**
     * Update Task in the data source
     */
    suspend fun updateTask(task: Task)
}