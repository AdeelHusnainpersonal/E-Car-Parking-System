package com.parkme.findparking.ui.fragments.auth.fillprofile

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.parkme.findparking.R
import com.parkme.findparking.databinding.FragmentFillProfileBinding
import com.parkme.findparking.utils.DateTimeUtils
import com.parkme.findparking.utils.Dialogs
import com.parkme.findparking.utils.toast

class FillProfileFragment : Fragment() {
    private var _binding: FragmentFillProfileBinding? = null
    private val binding get() = _binding!!
    private val ccp by lazy { binding.countryCodePicker }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFillProfileBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
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
            if (validateForm()) {
                toast("Loged in")
            }
        }
    }

    private fun validateForm(): Boolean {
        ccp.registerCarrierNumberEditText(binding.etPhoneNumber)

        val fullNameEditText = binding.etFullName
        val dateOfBirthEditText = binding.etDateOfBirth
        val addressEditText = binding.etAddress
        val phoneNumberEditText = binding.etPhoneNumber

        fullNameEditText.error = null
        dateOfBirthEditText.error = null
        addressEditText.error = null

        return when {
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