package com.ecoatm.devicepickup.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ecoatm.devicepickup.model.ShippingBoxModel

@Database(entities = [ShippingBoxModel::class], version = 1)
abstract class DevicePickupDatabase : RoomDatabase() {
    abstract fun shippingBoxDao(): ShippingBoxDao
}