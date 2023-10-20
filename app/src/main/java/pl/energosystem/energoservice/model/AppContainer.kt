package pl.energosystem.energoservice.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pl.energosystem.energoservice.model.service.AccountService
import pl.energosystem.energoservice.model.service.ProtocolStorageService
import pl.energosystem.energoservice.model.service.TaskStorageService
import pl.energosystem.energoservice.model.service.impl.AccountServiceImpl
import pl.energosystem.energoservice.model.service.impl.ProtocolStorageServiceImpl
import pl.energosystem.energoservice.model.service.impl.TaskStorageServiceImpl

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val accountService: AccountService
    val taskStorageService: TaskStorageService
    val protocolStorageService: ProtocolStorageService
}

/**
 * [AppContainer] implementation that provides instance of
 * [AccountService] [TaskStorageService] [ProtocolStorageService]
 */
class AppDataContainer : AppContainer {

    /**
     * Implementation for [AccountService]
     */
    override val accountService: AccountService by lazy {
        AccountServiceImpl(Firebase.auth)
    }

    /**
     * Implementation for [TaskStorageService]
     */
    override val taskStorageService: TaskStorageService by lazy {
        TaskStorageServiceImpl(Firebase.firestore, accountService)
    }

    /**
     * Implementation for [ProtocolStorageService]
     */
    override val protocolStorageService: ProtocolStorageServiceImpl by lazy {
        ProtocolStorageServiceImpl(Firebase.firestore, accountService)
    }
}