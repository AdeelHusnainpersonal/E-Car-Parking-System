package com.parkme.findparking.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.parkme.findparking.R
import com.parkme.findparking.data.ModelParking
import com.parkme.findparking.databinding.FragmentHomeBinding
import com.parkme.findparking.preferences.PreferenceManager
import com.parkme.findparking.utils.Constants
import com.parkme.findparking.utils.DataState
import com.parkme.findparking.utils.ProgressDialogUtil.dismissProgressDialog
import com.parkme.findparking.utils.ProgressDialogUtil.showProgressDialog
import com.parkme.findparking.utils.toast
import com.parkme.findparking.utils.withHi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var preferenceManager: PreferenceManager
    private val vmHome: VmHome by viewModels()

    private lateinit var adapter: AdapterParking
    private var parkingList = listOf<ModelParking>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        setUserName()
        setUpAdapter()
    }

    private fun setUpSearch() {
        val searchView = binding.etSearch
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.getFilter().filter(newText)
                return true
            }
        })
        adapter.updateData(parkingList)
    }

    private fun setUpAdapter() {
        vmHome.parkingSpaces.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    val listOfParking = it.data
                    if (listOfParking != null) {
                        parkingList = listOfParking
                        adapter = AdapterParking(listOfParking) { modelParking ->
                            val bundle = Bundle()
                            bundle.putString(Constants.KEY_PARKING_NAME, modelParking.spaceName)
                            bundle.putString(
                                Constants.KEY_SPACE_LOCATION,
                                modelParking.spaceMapLocation
                            )
                            bundle.putString(Constants.KEY_SPACE_DOCID, modelParking.docId)
                            findNavController().navigate(
                                R.id.action_homeFragment_to_spotsFragment,
                                bundle
                            )
                        }
                        binding.rvParking.adapter = adapter
                        dismissProgressDialog()
                        setUpSearch()
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

    private fun setUserName() {
        val user = preferenceManager.getUserData()
        if (user != null) {
            binding.tvUserName.text = user.fullName.withHi()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}