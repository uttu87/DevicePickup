package com.ecoatm.devicepickup.repository

import androidx.lifecycle.LiveData
import com.ecoatm.devicepickup.model.LoginUserModel
import com.ecoatm.devicepickup.model.ShippingBoxModel
import com.ecoatm.devicepickup.utils.Resource

interface DevicePickupRepositoryInterface {

    suspend fun login(userId: String, password: String) : Resource<LoginUserModel>

    suspend fun insertShippingBox(box: ShippingBoxModel)

    suspend fun deleteShippingBox(box: ShippingBoxModel)

    fun getShippingBox(userId: String) : LiveData<List<ShippingBoxModel>>


}