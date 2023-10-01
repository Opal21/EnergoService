package pl.energosystem.energoservice.data.user

import kotlinx.coroutines.flow.Flow

class OfflineUsersRepository(private val userDao: UserDao): UsersRepository {
    override fun getAllUsersStream(): Flow<List<User>> = userDao.getAllUsers()

    override suspend fun getUser(email: String, password: String): User? =
        userDao.getUserByEmailPassword(email, password)

    override suspend fun insertUser(user: User) = userDao.insert(user)

    override suspend fun deleteUser(user: User) = userDao.delete(user)

    override suspend fun updateUser(user: User) = userDao.update(user)
}