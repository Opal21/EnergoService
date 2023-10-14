package pl.energosystem.energoservice.data.protocol

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.energosystem.energoservice.ui.protocol.ServiceType

@Entity(tableName = "protocols_db")
data class Protocol(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "locator_name") val locatorName: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "room") val room: String,
    @ColumnInfo(name = "comments") val comments: String,
    @ColumnInfo(name = "service_type") val serviceType: ServiceType,
//    @ColumnInfo(name = "serviced_device") val servicedDevice: String,
//    @ColumnInfo(name = "new_device") val newDevice: Device?
)
