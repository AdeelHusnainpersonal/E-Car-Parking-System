package com.parkme.findparking.ui.fragments.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.parkme.findparking.data.ModelBookedCar
import com.parkme.findparking.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VmBookingSpot @Inject constructor(
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _bookingStatus = MutableLiveData<DataState<Nothing>>()
    val bookingStatus: LiveData<DataState<Nothing>> = _bookingStatus

    private val _bookedSpots = MutableLiveData<DataState<List<ModelBookedCar>>>()
    val bookedSpots: LiveData<DataState<List<ModelBookedCar>>> = _bookedSpots

    fun bookSpot(booking: ModelBookedCar) {
        _bookingStatus.value = DataState.Loading

        val startTimeMillis = System.currentTimeMillis()
        val durationHours = booking.durationHours
        // Convert duration from hours to milliseconds
        val durationMillis = durationHours * 3600000
        // Calculate the end time
        val endTimeMillis = startTimeMillis + durationMillis
        // Convert end time to seconds
        val endTimeSeconds = endTimeMillis / 1000

        booking.endTimeSeconds = endTimeSeconds

        db.collection("bookedSpots").add(booking)
            .addOnSuccessListener {
                val spotName = booking.spotName
                val parkingSpaceName = booking.parkingSpaceName

                db.collection("parkingSpaces").document(booking.docId).collection("spots")
                    .document(spotName)
                    .update("booked", true)
                    .addOnSuccessListener {
                        _bookingStatus.value = DataState.Success()
                    }
                    .addOnFailureListener { exception ->
                        _bookingStatus.value =
                            DataState.Error(exception.message ?: "Failed to update spot status")
                    }
            }
            .addOnFailureListener {
                _bookingStatus.value = DataState.Error(it.message!!)
            }


    }
}