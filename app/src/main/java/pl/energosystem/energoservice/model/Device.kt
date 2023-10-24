package pl.energosystem.energoservice.model

data class Device(
    val type: String = "",
    val location: String = "",
    val serialNumber: Long = 0,
    val readout: Double = 0.0,
)