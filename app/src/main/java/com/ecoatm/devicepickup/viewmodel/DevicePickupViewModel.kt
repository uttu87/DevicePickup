package com.ecoatm.devicepickup.viewmodel

import android.text.TextUtils
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ecoatm.devicepickup.repository.DevicePickupRepository
import javax.inject.Inject

class DevicePickupViewModel @Inject constructor(
    private val repository: DevicePickupRepository
) : ViewModel(), Observable {

    @Bindable
    var kioskQRCode = MutableLiveData<String>()

    @Bindable
    var missedPickupReason = MutableLiveData<String>()

    private val _btnContinueEnable = MutableLiveData<Boolean>()
    val btnContinueEnable: LiveData<Boolean>
        get() = _btnContinueEnable

    private val _btnSubmitEnable = MutableLiveData<Boolean>()
    val btnSubmitEnable: LiveData<Boolean>
        get() = _btnSubmitEnable

    fun btnContinuePressed() {

    }

    fun submit() {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        //Do Nothing
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        //Do Nothing
    }

}