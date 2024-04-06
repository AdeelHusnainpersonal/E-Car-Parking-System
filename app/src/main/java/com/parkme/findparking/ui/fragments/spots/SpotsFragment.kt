package com.parkme.findparking.ui.fragments.spots

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.parkme.findparking.R
import com.parkme.findparking.databinding.FragmentSpotsBinding
import com.parkme.findparking.ui.fragments.home.AdapterParking
import com.parkme.findparking.utils.Constants
import com.parkme.findparking.utils.DataState
import com.parkme.findparking.utils.ProgressDialogUtil.dismissProgressDialog
import com.parkme.findparking.utils.ProgressDialogUtil.showProgressDialog
import com.parkme.findparking.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpotsFragment : Fragment() {
    private var _binding: FragmentSpotsBinding? = null
    private val binding get() = _binding!!
    private val vmSpots: VmSpots by viewModels()
    private lateinit var parkingSpaceName: String
    private lateinit var parkingSpaceLocation: String
    private lateinit var parkingDocId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpotsBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        getNameAndSearchSpots()
        setUpObserver()
    }

    private fun setOnClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getNameAndSearchSpots() {
        val parkingName = arguments?.getString(Constants.KEY_PARKING_NAME)
        val parkingLocation = arguments?.getString(Constants.KEY_SPACE_LOCATION)
        val parkingDocId = arguments?.getString(Constants.KEY_SPACE_DOCID)
        if (parkingName != null && parkingLocation != null && parkingDocId != null) {
            this.parkingSpaceName = parkingName
            this.parkingSpaceLocation = parkingLocation
            this.parkingDocId = parkingDocId
            vmSpots.fetchSpotsForParkingSpace(parkingDocId)
        }
    }

    private fun setUpObserver() {
        vmSpots.spots.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    val listOfSpots = it.data
                    if (listOfSpots != null) {
                        binding.rvSpots.adapter = AdapterSpots(listOfSpots) { spotName ->
                            val bundle = Bundle()
                            bundle.putString(Constants.KEY_SPOT_NAME, spotName)
                            bundle.putString(Constants.KEY_SPACE_DOCID, parkingDocId)
                            bundle.putString(Constants.KEY_PARKING_NAME, parkingSpaceName)
                            bundle.putString(Constants.KEY_SPACE_LOCATION, parkingSpaceLocation)
                            findNavController().navigate(
                                R.id.action_spotsFragment_to_bookingFragment,
                                bundle
                            )
                        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}