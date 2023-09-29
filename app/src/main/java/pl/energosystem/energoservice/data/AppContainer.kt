package pl.energosystem.energoservice.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val protocolsRepository: ProtocolsRepository
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
}