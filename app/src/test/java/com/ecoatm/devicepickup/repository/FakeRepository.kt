package com.ecoatm.devicepickup.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ecoatm.devicepickup.model.LoginUserModel
import com.ecoatm.devicepickup.model.ShippingBoxModel
import com.ecoatm.devicepickup.utils.Resource

class FakeRepository : DevicePickupRepositoryInterface {
    private val boxes = mutableListOf<ShippingBoxModel>()
    private val boxesLiveData = MutableLiveData<List<ShippingBoxModel>>(boxes)

    override suspend fun login(userId: String, password: String): Resource<LoginUserModel> {
        return Resource.success(LoginUserModel(userId, password))
    }

    override suspend fun insertShippingBox(box: ShippingBoxModel) {
        boxes.add(box)
        refreshData()
    }

    override suspend fun deleteShippingBox(box: ShippingBoxModel) {
        boxes.remove(box)
        refreshData()
    }

    override fun getShippingBox(userId: String): LiveData<List<ShippingBoxModel>> {
        return boxesLiveData
    }

    private fun refreshData() {
        boxesLiveData.postValue(boxes)
    }
}