package pl.energosystem.energoservice.data.protocol

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProtocolDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(protocol: Protocol)

    @Update
    suspend fun update(protocol: Protocol)

    @Delete
    suspend fun delete(protocol: Protocol)

    @Query("SELECT * FROM protocols_db")
    fun getAllProtocols(): Flow<List<Protocol>>

    @Query("SELECT * FROM protocols_db WHERE id = :id")
    fun getProtocolByID(id: Int): Flow<Protocol>
}
