package com.parkme.findparking.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.parkme.findparking.R
import com.parkme.findparking.databinding.FragmentProfileBinding
import com.parkme.findparking.ui.activities.BeforeAuthActivity
import com.parkme.findparking.utils.Dialogs
import com.parkme.findparking.utils.startActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnLogOut.setOnClickListener {
            Dialogs.logOutDialog(requireContext(),layoutInflater){
                auth.signOut()
                startActivity(BeforeAuthActivity::class.java)
                requireActivity().finish()
            }
        }
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
        binding.btnMyCars.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_myCarsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}