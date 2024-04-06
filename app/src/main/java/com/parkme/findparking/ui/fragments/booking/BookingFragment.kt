package com.parkme.findparking.ui.fragments.booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.parkme.findparking.R
import com.parkme.findparking.data.ModelBookedCar
import com.parkme.findparking.data.ModelMyCar
import com.parkme.findparking.databinding.FragmentBookingBinding
import com.parkme.findparking.ui.fragments.profile.VmProfile
import com.parkme.findparking.utils.Constants
import com.parkme.findparking.utils.DataState
import com.parkme.findparking.utils.ProgressDialogUtil.dismissProgressDialog
import com.parkme.findparking.utils.ProgressDialogUtil.showProgressDialog
import com.parkme.findparking.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookingFragment : Fragment() {
    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!
    private lateinit var spotName: String
    private lateinit var parknigDocId: String
    private lateinit var parkingName: String
    private lateinit var parkingLocation: String
    private val vmProfile: VmProfile by viewModels()
    private val vmBookingSpot: VmBookingSpot by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        getSpotName()
        getAndShowCars()
        setUpObservers()
    }

    private fun setOnClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnBook.setOnClickListener {
            if (isValid()) {
                conformBooking()
            }
        }
    }

    private fun conformBooking() {
        val etCar = binding.spinnerSelectCar.text.toString()
        val (carName, carNumber) = etCar.split(", ")
        val time = binding.etTime.text.toString().toInt()
        val bookingModel = ModelBookedCar(
            carName,
            carNumber,
            parkingName,
            spotName,
            time,
            parkingLocation,
            parknigDocId
        )
        lifecycle.let {
            vmBookingSpot.bookSpot(bookingModel)
        }
    }

    private fun setUpObservers() {
        vmBookingSpot.bookingStatus.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    toast("Booked Successfully")
                    findNavController().navigate(R.id.action_bookingFragment_to_homeFragment)
                    dismissProgressDialog()
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

    private fun getAndShowCars() {
        lifecycleScope.launch {
            vmProfile.getCars.collectLatest {
                collectAndSetCars(it)
            }
        }
    }

    private fun collectAndSetCars(dataState: DataState<List<ModelMyCar>>) {
        when (dataState) {
            is DataState.Success -> {
                val carsList = dataState.data
                if (carsList != null) {
                    val carNames = carsList.map { "${it.carName}, ${it.carNumber}" }
                    binding.spinnerSelectCar.setOnClickListener {
                        val arrayAdapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            carNames
                        )
                        binding.spinnerSelectCar.setAdapter(arrayAdapter)
                        binding.spinnerSelectCar.showDropDown()
                    }
                }
            }

            is DataState.Error -> {
                toast(dataState.errorMessage)
            }

            is DataState.Loading -> {
            }
        }
    }

    private fun getSpotName() {
        val spotName = arguments?.getString(Constants.KEY_SPOT_NAME)
        val spaceName = arguments?.getString(Constants.KEY_PARKING_NAME)
        val spaceDocId = arguments?.getString(Constants.KEY_SPACE_DOCID)
        val spaceLocation = arguments?.getString(Constants.KEY_SPACE_LOCATION)
        if (spotName != null && spaceName != null && spaceLocation != null && spaceDocId != null) {
            this.spotName = spotName
            this.parkingName = spaceName
            this.parkingLocation = spaceLocation
            this.parknigDocId = spaceDocId
        }
    }

    private fun isValid(): Boolean {
        return when {
            binding.spinnerSelectCar.text.isNullOrEmpty() -> {
                toast("Select a car first")
                false
            }

            binding.etTime.text.isNullOrEmpty() -> {
                binding.etTime.error = "Enter time"
                binding.etTime.requestFocus()
                false
            }

            else -> {
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}