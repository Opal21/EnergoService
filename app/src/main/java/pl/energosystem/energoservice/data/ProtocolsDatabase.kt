package pl.energosystem.energoservice.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Protocol::class], version = 1, exportSchema = false)
abstract class ProtocolsDatabase: RoomDatabase() {
    abstract fun protocolDao(): ProtocolDao
    companion object{
        @Volatile
        private var Instance: ProtocolsDatabase? = null

        fun getDatabase(context: Context): ProtocolsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ProtocolsDatabase::class.java, "protocols_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}