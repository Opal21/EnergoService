package pl.energosystem.energoservice.data.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UsersDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object{
        @Volatile
        private var Instance: UsersDatabase? = null

        fun getDatabase(context: Context): UsersDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, UsersDatabase::class.java, "users_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}