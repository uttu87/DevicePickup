package com.ecoatm.devicepickup.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecoatm.devicepickup.R
import com.ecoatm.devicepickup.databinding.FragmentShippingInformationBinding

class ShippingInformationFragment : Fragment(R.layout.fragment_shipping_information) {

    private var fragmentBinding: FragmentShippingInformationBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentBinding = FragmentShippingInformationBinding.bind(view)
        fragmentBinding?.textView?.text  = "Shipping Information"
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

}