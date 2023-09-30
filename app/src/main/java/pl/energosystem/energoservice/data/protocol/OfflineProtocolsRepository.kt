package pl.energosystem.energoservice.data.protocol

import kotlinx.coroutines.flow.Flow

class OfflineProtocolsRepository(private val protocolDao: ProtocolDao): ProtocolsRepository {
    override fun getAllProtocolsStream(): Flow<List<Protocol>> = protocolDao.getAllProtocols()

    override fun getProtocolStream(id: Int): Flow<Protocol?> = protocolDao.getProtocolByID(id)

    override suspend fun insertProtocol(protocol: Protocol) = protocolDao.insert(protocol)

    override suspend fun deleteProtocol(protocol: Protocol) = protocolDao.delete(protocol)

    override suspend fun updateProtocol(protocol: Protocol) = protocolDao.update(protocol)
}