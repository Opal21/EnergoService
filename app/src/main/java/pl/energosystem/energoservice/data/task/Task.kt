package pl.energosystem.energoservice.data.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks_db")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "task_description") val text: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "task_date") val date: Date
)