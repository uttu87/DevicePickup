package com.ecoatm.devicepickup.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ecoatm.devicepickup.R
import com.ecoatm.devicepickup.databinding.FragmentShippingBoxBinding

class ShippingBoxFragment : Fragment(R.layout.fragment_shipping_box) {
    private var fragmentBinding: FragmentShippingBoxBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentBinding = FragmentShippingBoxBinding.bind(view)
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}