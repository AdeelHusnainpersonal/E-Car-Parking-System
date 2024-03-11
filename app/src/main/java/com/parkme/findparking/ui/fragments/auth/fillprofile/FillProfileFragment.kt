package com.parkme.findparking.ui.fragments.auth.fillprofile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.parkme.findparking.data.ModelUser
import com.parkme.findparking.databinding.FragmentFillProfileBinding
import com.parkme.findparking.ui.activities.AfterAuthActivity
import com.parkme.findparking.ui.fragments.auth.VmAuth
import com.parkme.findparking.utils.Constants
import com.parkme.findparking.utils.DataState
import com.parkme.findparking.utils.DateTimeUtils
import com.parkme.findparking.utils.Dialogs
import com.parkme.findparking.utils.ProgressDialogUtil.dismissProgressDialog
import com.parkme.findparking.utils.ProgressDialogUtil.showProgressDialog
import com.parkme.findparking.utils.startActivity
import com.parkme.findparking.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FillProfileFragment : Fragment() {
    private var _binding: FragmentFillProfileBinding? = null
    private val binding get() = _binding!!
    private val ccp by lazy { binding.countryCodePicker }
    private val scope = lifecycleScope
    private val vm by viewModels<VmAuth>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFillProfileBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        ccp.registerCarrierNumberEditText(binding.etPhoneNumber)
        setOnClickListener()
        setupObserver()
    }

    private fun setOnClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.etDateOfBirth.setOnClickListener {
            Dialogs.showDatePickerDialog(requireContext()){
                binding.etDateOfBirth.setText(DateTimeUtils.formatDateOfBirth(it))
            }
        }
        binding.btnContinue.setOnClickListener {
            if (validateForm()){
                login()
            }
        }
    }

    private fun login() {
        val email = arguments?.getString(Constants.KEY_EMAIL)!!
        val password = arguments?.getString(Constants.KEY_PASSWORD)!!
        val fullName = binding.etFullName.text.toString()
        val dateOfBirth = binding.etDateOfBirth.text.toString()
        val phoneNumber = ccp.fullNumberWithPlus
        val address = binding.etAddress.text.toString()

        val user = ModelUser(email,password,fullName,dateOfBirth,phoneNumber,address)

        scope.launch(Dispatchers.IO){
            vm.signUpWithEmailPass(user)
        }
    }

    private fun setupObserver() {
        vm.signUpStatus.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    toast("Account created successfully")
                    dismissProgressDialog()
                    startActivity(AfterAuthActivity::class.java)
                    requireActivity().finish()
                }

                is DataState.Error -> {
                    toast(dataState.errorMessage)
                    dismissProgressDialog()
                }

                is DataState.Loading -> {
                    showProgressDialog()
                }
            }
        }

    }

    private fun validateForm(): Boolean {
        val fullNameEditText = binding.etFullName
        val dateOfBirthEditText = binding.etDateOfBirth
        val addressEditText = binding.etAddress
        val phoneNumberEditText = binding.etPhoneNumber

        fullNameEditText.error = null
        dateOfBirthEditText.error = null
        addressEditText.error = null
        phoneNumberEditText.error = null

        return when {
            fullNameEditText.text.isNullOrEmpty() -> {
                fullNameEditText.error = "Please fill!"
                fullNameEditText.requestFocus()
                false
            }
            dateOfBirthEditText.text.isNullOrEmpty() ->{
                dateOfBirthEditText.error = "Please fill!"
                dateOfBirthEditText.requestFocus()
                false
            }
            addressEditText.text.isNullOrEmpty() ->{
                addressEditText.error = "Please fill!"
                addressEditText.requestFocus()
                false
            }
            phoneNumberEditText.text.isNullOrEmpty() ->{
                phoneNumberEditText.error = "Please fill!"
                phoneNumberEditText.requestFocus()
                false
            }
            !ccp.isValidFullNumber -> {
                toast("Invalid phone number for ${ccp.selectedCountryName}")
                false
            }
            else -> true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}