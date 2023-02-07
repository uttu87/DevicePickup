package com.ecoatm.devicepickup.utils

import com.ecoatm.devicepickup.BuildConfig

object Constants {
    const val BASE_URL = "https://ecoatm.com"

    //Room Database Constants
    const val DATABASE_NAME = "DevicePickupDB"
    const val SHIPPING_BOX_TABLE_NAME = "shipping_box_table"

    const val APP_VERSION_TEXT = "Version: " + BuildConfig.VERSION_NAME
}