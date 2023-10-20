package pl.energosystem.energoservice.model.service

import kotlinx.coroutines.flow.Flow
import pl.energosystem.energoservice.model.Protocol

interface ProtocolStorageService {
    val protocols: Flow<List<Protocol>>
    suspend fun getProtocol(protocolId: String) : Protocol?
    suspend fun save(protocol: Protocol) : String
    suspend fun update(protocol: Protocol)
    suspend fun delete(protocolId: String)
}