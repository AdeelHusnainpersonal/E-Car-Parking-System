package com.parkme.findparking.ui.fragments.auth.signup

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.parkme.findparking.R
import com.parkme.findparking.databinding.FragmentSignUpBinding
import com.parkme.findparking.utils.toast

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
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
        binding.tvSignIn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSignup.setOnClickListener {
            if (isValid()){
                findNavController().navigate(R.id.action_signUpFragment_to_fillProfileFragment)
            }
        }
    }

    private fun isValid():Boolean{
        binding.etEmail.error = null
        binding.etPassword.error = null
        return when{
            binding.etEmail.text.isNullOrEmpty() -> {
                binding.etEmail.error = "Please fill!"
                binding.etEmail.requestFocus()
                false
            }
            binding.etPassword.text.isNullOrEmpty() ->{
                binding.etPassword.error = "Please fill!"
                binding.etPassword.requestFocus()
                false
            }
            binding.etPassword.text.toString().length < 7 ->{
                binding.etPassword.error = "Password must be at least 7 characters long"
                binding.etPassword.requestFocus()
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches() ->{
                binding.etEmail.error = "Invalid email pattern"
                binding.etEmail.requestFocus()
                false
            }

            else ->{
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}