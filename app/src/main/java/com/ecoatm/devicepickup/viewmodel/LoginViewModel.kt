package com.ecoatm.devicepickup.viewmodel

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoatm.devicepickup.model.LoginUserModel
import com.ecoatm.devicepickup.repository.DevicePickupRepositoryInterface
import com.ecoatm.devicepickup.utils.Constants
import com.ecoatm.devicepickup.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: DevicePickupRepositoryInterface
) : ViewModel(), Observable {

    var loginUserModel: LoginUserModel? = null

    private var _loginStatus = MutableLiveData<Resource<LoginUserModel>>()
    val loginStatus: LiveData<Resource<LoginUserModel>>
        get() = _loginStatus

    val userId = MutableLiveData<String>()

    val userPIN = MutableLiveData<String>()

    val version = Constants.APP_VERSION_TEXT

    private val _rememberMe = MutableLiveData<Boolean>()
    val rememberMe: LiveData<Boolean>
        get() = _rememberMe

    private val _btnGoEnable = MutableLiveData<Boolean>()
    val btnGoEnable: LiveData<Boolean>
        get() = _btnGoEnable

    private fun checkBtnGoEnable() {
        _btnGoEnable.value = !TextUtils.isEmpty(userId.value) && !TextUtils.isEmpty(userPIN.value)
    }

    fun rememberMeChanged() {
        if (_rememberMe.value == null) {
            _rememberMe.value = false
        }
        _rememberMe.value = !_rememberMe.value!!
    }

    fun goPressed() {
        _loginStatus.postValue(Resource.loading(null))
        if (userId.value == null || userPIN.value == null) {
            _loginStatus.postValue(Resource.error("Login failed!", null))
            return
        }
        viewModelScope.launch {
            //_loginStatus.value =
            repository.login(userId.value!!, userId.value!!)
            //Force always success
            _loginStatus.value = Resource.success(LoginUserModel(userId.value!!, userPIN.value!!))
//            _loginStatus.value = Resource.error("Login failed!", null)
        }
    }

    fun resetLoginStatus() {
        _loginStatus = MutableLiveData<Resource<LoginUserModel>>()
    }

    @get:Bindable
    val userIdTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // Do nothing.
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            userId.value = s.toString()
            checkBtnGoEnable()
        }

        override fun afterTextChanged(s: Editable) {
            // Do nothing.
        }
    }

    @get:Bindable
    val userPINTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // Do nothing.
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            userPIN.value = s.toString()
            checkBtnGoEnable()
        }

        override fun afterTextChanged(s: Editable) {
            // Do nothing.
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}