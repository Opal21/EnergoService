package pl.energosystem.energoservice.model

import com.google.firebase.firestore.DocumentId

data class Protocol(
    @DocumentId val id: String = "",
    val title: String = "",
    val creationDate: String = "",
    val description: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val locatorsName: String = "",
    val location: String = "",
    val userId: String = "",
    val oldDeviceReadout: Double = 0.0,
    val newDeviceReadout: Double = 0.0,
    val completed: Boolean = false,
    val oldDevice: Device? = null,
    val newDevice: Device? = null,
)
