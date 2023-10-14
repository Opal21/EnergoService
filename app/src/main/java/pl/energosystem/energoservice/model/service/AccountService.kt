package pl.energosystem.energoservice.model.service

import kotlinx.coroutines.flow.Flow
import pl.energosystem.energoservice.model.User

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<User>

    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}