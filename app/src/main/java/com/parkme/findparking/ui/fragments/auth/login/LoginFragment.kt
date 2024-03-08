package com.parkme.findparking.ui.fragments.auth.login

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.parkme.findparking.R
import com.parkme.findparking.databinding.FragmentLoginBinding
import com.parkme.findparking.utils.BackPressedUtils.goBackPressed
import com.parkme.findparking.utils.toast

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        backPressed()
    }

    private fun setOnClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnLogin.setOnClickListener {
            if (isValid()){
                toast("Login")
            }
        }
        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    private fun backPressed(){
        goBackPressed {
            requireActivity().finishAffinity()
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