package com.parkme.findparking.ui.fragments.spots

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.parkme.findparking.data.ModelSpots
import com.parkme.findparking.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VmSpots @Inject constructor(
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _spots = MutableLiveData<DataState<List<ModelSpots>>>()
    val spots: LiveData<DataState<List<ModelSpots>>> get() = _spots

    fun fetchSpotsForParkingSpace(parkingSpaceDocId: String) {
        _spots.value = DataState.Loading

        db.collection("parkingSpaces")
            .document(parkingSpaceDocId)
            .collection("spots")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    _spots.value = DataState.Error("Failed to fetch spots: ${error.message}")
                    return@addSnapshotListener
                }

                val spotsList = mutableListOf<ModelSpots>()
                querySnapshot?.forEach { document ->
                    val spot = document.toObject(ModelSpots::class.java)
                    spotsList.add(spot)
                }

                // Sort spots numerically based on spotName
                spotsList.sortBy { spot ->
                    val numberPart = spot.spotName.substring(1).toIntOrNull() ?: Int.MAX_VALUE
                    numberPart
                }

                _spots.value = DataState.Success(spotsList)
            }
    }

}