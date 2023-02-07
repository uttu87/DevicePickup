package com.ecoatm.devicepickup.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ecoatm.devicepickup.utils.Constants.SHIPPING_BOX_TABLE_NAME
import java.util.UUID

@Entity(tableName = SHIPPING_BOX_TABLE_NAME)
data class ShippingBoxModel(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo("shipping_box_id")
    val shippingBoxId: Int,
    @ColumnInfo("tracking_number")
    val trackingNumber: String,
    @ColumnInfo("shipping_provider")
    val shippingProvider: String,
    @ColumnInfo("number_of_devices")
    val numberOfDevices: Int,
    @ColumnInfo("user_id")
    val userId: String,
    @ColumnInfo("timestamp")
    val timestamp: Long = System.currentTimeMillis()
)