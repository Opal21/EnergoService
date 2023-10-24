package pl.energosystem.energoservice.model

import com.google.firebase.firestore.DocumentId

data class Protocol(
    @DocumentId val id: String = "",
    val title: String = "",
    val creationDate: String = "",
    val description: String = "",
    val comments: String = "",
    val address: String = "",
    val locatorsPhoneNumber: String = "",
    val locatorsName: String = "",
    val userId: String = "",
    val taskId: String = "",
    val completed: Boolean = false,
    val oldDevice: Device? = null,
    val newDevice: Device? = null,
)
