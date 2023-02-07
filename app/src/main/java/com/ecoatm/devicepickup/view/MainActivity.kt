package com.ecoatm.devicepickup.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.ecoatm.devicepickup.R
import com.ecoatm.devicepickup.repository.DevicePickupRepository
import com.ecoatm.devicepickup.repository.DevicePickupRepositoryInterface
import com.ecoatm.devicepickup.viewmodel.ViewModelsFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.toolbar_layout)
        setContentView(R.layout.activity_main)
    }
}