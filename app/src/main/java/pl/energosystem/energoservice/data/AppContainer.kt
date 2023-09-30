package pl.energosystem.energoservice.data

import android.content.Context
import pl.energosystem.energoservice.data.protocol.OfflineProtocolsRepository
import pl.energosystem.energoservice.data.protocol.ProtocolsDatabase
import pl.energosystem.energoservice.data.protocol.ProtocolsRepository
import pl.energosystem.energoservice.data.task.OfflineTasksRepository
import pl.energosystem.energoservice.data.task.TasksDatabase
import pl.energosystem.energoservice.data.task.TasksRepository
import pl.energosystem.energoservice.data.user.OfflineUsersRepository
import pl.energosystem.energoservice.data.user.UsersDatabase
import pl.energosystem.energoservice.data.user.UsersRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val protocolsRepository: ProtocolsRepository
    val tasksRepository: TasksRepository
    val usersRepository: UsersRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineProtocolsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {

    /**
     * Implementation for [ProtocolsRepository]
     */
    override val protocolsRepository: ProtocolsRepository by lazy {
        OfflineProtocolsRepository(ProtocolsDatabase.getDatabase(context).protocolDao())
    }

    /**
     * Implementation for [TasksRepository]
     */
    override val tasksRepository: TasksRepository by lazy {
        OfflineTasksRepository(TasksDatabase.getDatabase(context).taskDao())
    }

    /**
     * Implementation for [UsersRepository]
     */
    override val usersRepository: UsersRepository by lazy {
        OfflineUsersRepository(UsersDatabase.getDatabase(context).userDao())
    }
}