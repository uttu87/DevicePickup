package com.ecoatm.devicepickup.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ecoatm.devicepickup.R
import com.ecoatm.devicepickup.databinding.FragmentDevicePickupBinding
import com.ecoatm.devicepickup.utils.setToolbarTitle
import com.ecoatm.devicepickup.viewmodel.DevicePickupViewModel
import com.ecoatm.devicepickup.viewmodel.ViewModelsFactory
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DevicePickupFragment : Fragment(R.layout.fragment_device_pickup) {

    private var binding: FragmentDevicePickupBinding? = null

    @Inject
    lateinit var factory : ViewModelsFactory
    lateinit var viewModel: DevicePickupViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setToolbarTitle(getString(R.string.device_pickup))
        viewModel = ViewModelProvider(requireActivity(), factory)[DevicePickupViewModel::class.java]
        binding = FragmentDevicePickupBinding.bind(view)
        binding?.viewModel = viewModel

        binding?.apply {
            tvScanOrEnterQRCode.setOnClickListener {
                notMerchandisedContainer.collapse(false)
                if (scanOrEnterQRCodeContainer.isExpanded) {
                    scanOrEnterQRCodeContainer.collapse(true)
                } else {
                    scanOrEnterQRCodeContainer.expand(true)
                }
            }

            tvNotMerchandised.setOnClickListener {
                scanOrEnterQRCodeContainer.collapse(false)
                if (notMerchandisedContainer.isExpanded) {
                    notMerchandisedContainer.collapse(true)
                } else {
                    notMerchandisedContainer.expand(true)
                }
            }

            kioskQRCodeEditText.setOnClickListener {
                onScanButtonClicked()
            }

            initViews()
            val callBack = object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }

            requireActivity().onBackPressedDispatcher.addCallback(callBack)

        }
    }

    private fun initViews() {
        initMissedPickupReasonViews()
        initEstimatedPickupDateViews()
    }

    private fun initEstimatedPickupDateViews() {
        val languages = resources.getStringArray(R.array.reason_items)

        // access the spinner
        val spinner = binding?.missedPickupReasonMenu
        if (spinner != null) {
            val adapter = ArrayAdapter(requireActivity(),
                android.R.layout.simple_spinner_item, languages)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    viewModel.missedPickupReason.value = languages[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    private fun initMissedPickupReasonViews() {
        val datePickup = DatePickerDialog(requireContext())
        datePickup.setOnDateSetListener { view, year, month, dayOfMonth ->

        }
        binding?.estimatedPickupDate?.setOnClickListener {
            datePickup.show()

        }
    }

    private fun onScanButtonClicked() {
        val optionsBuilder = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_ALL_FORMATS
            )
            .allowManualInput()
            .build()
        val gmsBarcodeScanner = GmsBarcodeScanning.getClient(requireActivity(), optionsBuilder)
        gmsBarcodeScanner
            .startScan()
            .addOnSuccessListener { barcode: Barcode ->
                viewModel.kioskQRCode.value = barcode.rawValue ?: ""
            }
            .addOnCanceledListener {
                binding?.kioskQRCodeEditText?.requestFocus()
            }
            .addOnFailureListener {
                binding?.kioskQRCodeEditText?.requestFocus()
            }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}