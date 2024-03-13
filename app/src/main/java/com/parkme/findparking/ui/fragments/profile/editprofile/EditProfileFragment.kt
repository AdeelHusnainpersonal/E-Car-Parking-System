package com.parkme.findparking.ui.fragments.profile.editprofile

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.parkme.findparking.data.ModelUser
import com.parkme.findparking.databinding.FragmentEditProfileBinding
import com.parkme.findparking.preferences.PreferenceManager
import com.parkme.findparking.ui.activities.AfterAuthActivity
import com.parkme.findparking.ui.fragments.profile.VmProfile
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
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var preferenceManager: PreferenceManager
    private val ccp by lazy { binding.countryCodePicker }
    private val vm:VmProfile by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        ccp.registerCarrierNumberEditText(binding.etPhoneNumber)
        setOnClickListener()
        setUserInfoInFields()
        setupObserver()
    }

    private fun setOnClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.etDateOfBirth.setOnClickListener {
            Dialogs.showDatePickerDialog(requireContext()){ date ->
                binding.etDateOfBirth.setText(DateTimeUtils.formatDateOfBirth(date))
            }
        }

        binding.btnUpdate.setOnClickListener {
            update()
        }
    }

    private fun update(){
        val userFromPref = preferenceManager.getUserData()

        val email = userFromPref?.email
        val password = userFromPref?.password
        val fullName = binding.etFullName.text.toString()
        val dateOfBirth = binding.etDateOfBirth.text.toString()
        val phoneNumber = ccp.fullNumberWithPlus
        val address = binding.etAddress.text.toString()

        if (email != null && password != null){
            val user = ModelUser(email,password,fullName,dateOfBirth,phoneNumber,address)

            lifecycleScope.launch(Dispatchers.IO){
                vm.updateUserProfile(user)
            }
        }
    }

    private fun setupObserver(){
        vm.updateStatus.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    toast("Updated successfully")
                    dismissProgressDialog()
                    findNavController().popBackStack()
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

    private fun setUserInfoInFields(){
        val user = preferenceManager.getUserData()
        if (user != null){
            binding.etFullName.setText(user.fullName)
            binding.etDateOfBirth.setText(user.dob)
            binding.etPhoneNumber.setText(user.phoneNumber)
            binding.etAddress.setText(user.address)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}