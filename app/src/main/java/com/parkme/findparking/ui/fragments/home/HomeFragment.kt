package com.parkme.findparking.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parkme.findparking.R
import com.parkme.findparking.databinding.FragmentHomeBinding
import com.parkme.findparking.preferences.PreferenceManager
import com.parkme.findparking.utils.withHi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        setUserName()
    }

    private fun setOnClickListener() {

    }

    private fun setUserName(){
        val user = preferenceManager.getUserData()
        if (user != null){
            binding.tvUserName.text = user.fullName.withHi()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}