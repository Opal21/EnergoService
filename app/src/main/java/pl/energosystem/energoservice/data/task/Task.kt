package pl.energosystem.energoservice.data.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_db")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "short_description") val shortDescription: String,
    @ColumnInfo(name = "long_description") val longDescription: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "phone_number") val phoneNumber: String,
    @ColumnInfo(name = "task_date") val date: String,
    @ColumnInfo(name = "is_done") val isDone: Boolean
)