package com.ecoatm.devicepickup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ecoatm.devicepickup.repository.DevicePickupRepository
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ViewModelsFactory @Inject constructor(
    private  val repository: DevicePickupRepository
): ViewModelProvider.Factory{
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        } else if(modelClass.isAssignableFrom(DevicePickupViewModel::class.java)) {
            return DevicePickupViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}