package com.ecoatm.devicepickup.repository

import androidx.lifecycle.LiveData
import com.ecoatm.devicepickup.repository.api.RetrofitAPI
import com.ecoatm.devicepickup.model.LoginUserModel
import com.ecoatm.devicepickup.model.ShippingBoxModel
import com.ecoatm.devicepickup.repository.db.ShippingBoxDao
import com.ecoatm.devicepickup.utils.Resource
import javax.inject.Inject

class DevicePickupRepository @Inject constructor(
    private val shippingBoxDao: ShippingBoxDao,
    private val retrofitAPI: RetrofitAPI
) : DevicePickupRepositoryInterface {
    override suspend fun login(userId: String, password: String): Resource<LoginUserModel> {
        return try {
            val response = retrofitAPI.login(userId, password)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Login Failed!", null)
            } else {
                Resource.error("Login Failed!", null)
            }
        } catch (e: Exception) {
            Resource.error("No Internet Connection!", null)
        }
    }

    override suspend fun insertShippingBox(box: ShippingBoxModel) {
        shippingBoxDao.insertShippingBox(box)
    }

    override suspend fun deleteShippingBox(box: ShippingBoxModel) {
        shippingBoxDao.deleteShippingBox(box)
    }

    override fun getShippingBox(userId: String): LiveData<List<ShippingBoxModel>> {
        return  shippingBoxDao.observeShippingBoxes(userId)
    }
}