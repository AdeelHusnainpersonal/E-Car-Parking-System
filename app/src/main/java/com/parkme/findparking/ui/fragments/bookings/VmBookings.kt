package com.parkme.findparking.ui.fragments.bookings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.parkme.findparking.data.ModelBookedCar
import com.parkme.findparking.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VmBookings @Inject constructor(
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _bookedSpots = MutableLiveData<DataState<List<ModelBookedCar>>>()
    val bookedSpots: LiveData<DataState<List<ModelBookedCar>>> = _bookedSpots

    init {
        viewModelScope.launch {
            fetchBookedSpots()
        }
    }

    private fun fetchBookedSpots() {
        _bookedSpots.value = DataState.Loading
        db.collection("bookedSpots")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    _bookedSpots.value =
                        DataState.Error(error.message ?: "Failed to fetch booked spots")
                    return@addSnapshotListener
                }

                val bookedSpotsList = mutableListOf<ModelBookedCar>()
                querySnapshot?.forEach { document ->
                    val bookedSpot = document.toObject(ModelBookedCar::class.java)
                    val docId = document.id
                    bookedSpot.docId = docId
                    bookedSpotsList.add(bookedSpot)
                }

                _bookedSpots.value = DataState.Success(bookedSpotsList)
            }
    }
}