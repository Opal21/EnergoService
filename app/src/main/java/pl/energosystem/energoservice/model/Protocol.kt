package pl.energosystem.energoservice.model

import com.google.firebase.firestore.DocumentId

data class Protocol(
    @DocumentId val id: String = "",
    val title: String = "",
    val creationDate: String = "",
    val description: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val completed: Boolean = false,
    val userId: String = ""
)
