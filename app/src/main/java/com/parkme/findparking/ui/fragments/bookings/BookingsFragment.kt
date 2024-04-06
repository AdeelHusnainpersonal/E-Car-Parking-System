package com.parkme.findparking.ui.fragments.bookings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.parkme.findparking.R
import com.parkme.findparking.databinding.FragmentBookingsBinding
import com.parkme.findparking.ui.fragments.booking.VmBookingSpot
import com.parkme.findparking.ui.fragments.home.AdapterParking
import com.parkme.findparking.utils.Constants
import com.parkme.findparking.utils.DataState
import com.parkme.findparking.utils.ProgressDialogUtil.dismissProgressDialog
import com.parkme.findparking.utils.ProgressDialogUtil.showProgressDialog
import com.parkme.findparking.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingsFragment : Fragment() {
    private var _binding: FragmentBookingsBinding? = null
    private val binding get() = _binding!!
    private val vmBookings: VmBookings by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBookingsBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        setUpObserver()
    }

    private fun setUpObserver() {
        vmBookings.bookedSpots.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    val listOfBookings = it.data
                    if (listOfBookings != null) {
                        binding.rvBookedSpots.adapter = AdapterBookedSpots(listOfBookings)
                        dismissProgressDialog()
                    }
                }

                is DataState.Error -> {
                    toast(it.errorMessage)
                    dismissProgressDialog()
                }

                is DataState.Loading -> {
                    showProgressDialog()
                }
            }
        }
    }

    private fun setOnClickListener() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}