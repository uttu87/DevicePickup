package com.ecoatm.devicepickup.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ecoatm.devicepickup.R
import com.ecoatm.devicepickup.databinding.FragmentLoginBinding
import com.ecoatm.devicepickup.utils.Status
import com.ecoatm.devicepickup.utils.setToolbarTitle
import com.ecoatm.devicepickup.viewmodel.LoginViewModel
import com.ecoatm.devicepickup.viewmodel.ViewModelsFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {

    private var binding: FragmentLoginBinding? = null
    @Inject
    lateinit var factory: ViewModelsFactory
    private lateinit var viewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val factory = LoginViewModelFactory((activity as MainActivity).repository)
        requireActivity().setToolbarTitle(getString(R.string.login))
        viewModel = ViewModelProvider(requireActivity(), factory)[LoginViewModel::class.java]
        binding = FragmentLoginBinding.bind(view)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        viewModel.loginStatus.observe (viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDevicePickupFragment())
                    viewModel.resetLoginStatus()
                }
                Status.ERROR -> {
                    binding?.progressBar?.visibility = View.GONE
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(getString(R.string.error))
                    builder.setMessage(it.message)
                    builder.show()
                }
                Status.LOADING -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}