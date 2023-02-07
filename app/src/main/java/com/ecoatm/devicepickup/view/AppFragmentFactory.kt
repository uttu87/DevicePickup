package com.ecoatm.devicepickup.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.ecoatm.devicepickup.repository.DevicePickupRepository
import javax.inject.Inject

class AppFragmentFactory : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            LoginFragment::class.java.name -> LoginFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}