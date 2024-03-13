package com.parkme.findparking.ui.fragments.profile.mycars

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.parkme.findparking.R
import com.parkme.findparking.data.ModelMyCar
import com.parkme.findparking.databinding.FragmentMyCarsBinding
import com.parkme.findparking.ui.fragments.profile.VmProfile
import com.parkme.findparking.utils.DataState
import com.parkme.findparking.utils.Dialogs
import com.parkme.findparking.utils.ProgressDialogUtil.dismissProgressDialog
import com.parkme.findparking.utils.ProgressDialogUtil.showProgressDialog
import com.parkme.findparking.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyCarsFragment : Fragment() {
    private var _binding: FragmentMyCarsBinding? = null
    private val binding get() = _binding!!
    private val vm:VmProfile by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyCarsBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
        setUpAdapter()
    }

    private fun setOnClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvAddNewCar.setOnClickListener {
            Dialogs.addCarDialog(requireContext(),layoutInflater) { carName, carNumber ->
                addCarToDb(carName,carNumber)
            }
        }
    }

    private fun addCarToDb(carName: String, carNumber: String) {
        val modelCar = ModelMyCar(carName,carNumber)
        lifecycleScope.launch(Dispatchers.IO){
            vm.addCar(modelCar){
                toast(it)
            }
        }
    }


    private fun setUpAdapter(){
        vm.getCars.observe(viewLifecycleOwner){
            when(it){
                is DataState.Success ->{
                    val carsList = it.data
                    carsList?.let { cars ->
                        val adapter = AdapterMyCars(cars)
                        binding.rvMyCars.adapter = adapter
                        dismissProgressDialog()
                    }
                }
                is DataState.Error ->{
                    toast(it.errorMessage)
                    dismissProgressDialog()
                }
                is DataState.Loading ->{
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