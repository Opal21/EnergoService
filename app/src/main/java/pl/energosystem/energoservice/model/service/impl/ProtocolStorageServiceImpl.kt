package pl.energosystem.energoservice.model.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import pl.energosystem.energoservice.model.Protocol
import pl.energosystem.energoservice.model.service.AccountService
import pl.energosystem.energoservice.model.service.ProtocolStorageService

class ProtocolStorageServiceImpl
constructor(private val firestore: FirebaseFirestore, private val auth: AccountService) : 
ProtocolStorageService {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val protocols: Flow<List<Protocol>>
        get() = auth.currentUser.flatMapLatest { user ->
            firestore.collection(PROTOCOL_COLLECTION).whereEqualTo(USER_ID_FIELD, user.id).dataObjects()
        }

    override suspend fun getProtocol(protocolId: String): Protocol? =
        firestore.collection(PROTOCOL_COLLECTION).document(protocolId).get().await().toObject()

    override suspend fun save(protocol: Protocol): String {
        val protocolWithUserId = protocol.copy(userId = auth.currentUserId)
        return firestore.collection(PROTOCOL_COLLECTION).add(protocolWithUserId).await().id
    }

    override suspend fun update(protocol: Protocol) {
        firestore.collection(PROTOCOL_COLLECTION).document(protocol.id).set(protocol).await()
    }

    override suspend fun delete(protocolId: String) {
        firestore.collection(PROTOCOL_COLLECTION).document(protocolId).delete().await()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val PROTOCOL_COLLECTION = "protocols"
        private const val SAVE_PROTOCOL_TRACE = "saveProtocol"
        private const val UPDATE_PROTOCOL_TRACE = "updateProtocol"
    }
}
