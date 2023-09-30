package pl.energosystem.energoservice.data.task

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks_db")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks_db WHERE id = :id")
    fun getTaskByID(id: Int): Flow<Task>

    @Query("SELECT * FROM tasks_db WHERE task_date = :date")
    fun getTasksByDate(date: Date): Flow<List<Task>>
}