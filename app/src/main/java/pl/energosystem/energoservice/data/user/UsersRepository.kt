package pl.energosystem.energoservice.data.user

import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    /**
     * Retrieve all the Users from the the given data source.
     */
    fun getAllUsersStream(): Flow<List<User>>

    /**
     * Retrieve an User from the given data source that matches with the [email] and [password].
     */
    suspend fun getUser(email: String, password: String): User?

    /**
     * Insert User in the data source
     */
    suspend fun insertUser(user: User)

    /**
     * Delete User from the data source
     */
    suspend fun deleteUser(user: User)

    /**
     * Update User in the data source
     */
    suspend fun updateUser(user: User)
}