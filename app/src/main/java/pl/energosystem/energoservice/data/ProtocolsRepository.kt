package pl.energosystem.energoservice.data

import kotlinx.coroutines.flow.Flow

interface ProtocolsRepository {

    /**
     * Retrieve all the Protocols from the the given data source.
     */
    fun getAllProtocolsStream(): Flow<List<Protocol>>

    /**
     * Retrieve an Protocol from the given data source that matches with the [id].
     */
    fun getProtocolStream(id: Int): Flow<Protocol?>

    /**
     * Insert Protocol in the data source
     */
    suspend fun insertProtocol(protocol: Protocol)

    /**
     * Delete Protocol from the data source
     */
    suspend fun deleteProtocol(protocol: Protocol)

    /**
     * Update Protocol in the data source
     */
    suspend fun updateProtocol(protocol: Protocol)
}