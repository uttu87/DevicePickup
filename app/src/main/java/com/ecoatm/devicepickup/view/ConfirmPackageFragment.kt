package com.ecoatm.devicepickup.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ecoatm.devicepickup.R
import com.ecoatm.devicepickup.databinding.FragmentConfirmPackageBinding

class ConfirmPackageFragment : Fragment(R.layout.fragment_confirm_package) {

    private var fragmentBinding: FragmentConfirmPackageBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentBinding = FragmentConfirmPackageBinding.bind(view)
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}