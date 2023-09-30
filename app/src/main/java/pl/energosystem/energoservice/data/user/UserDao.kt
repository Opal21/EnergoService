package pl.energosystem.energoservice.data.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users_db")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users_db WHERE id = :id")
    fun getUserByID(id: Int): Flow<User>
}