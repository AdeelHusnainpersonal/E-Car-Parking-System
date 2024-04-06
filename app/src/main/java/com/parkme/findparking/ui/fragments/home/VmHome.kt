package com.parkme.findparking.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.parkme.findparking.data.ModelParking
import com.parkme.findparking.utils.DataState
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VmHome @Inject constructor(
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _parkingSpaces = MutableLiveData<DataState<List<ModelParking>>>()
    val parkingSpaces: LiveData<DataState<List<ModelParking>>> get() = _parkingSpaces

    init {
        viewModelScope.launch {
            fetchParkingSpaces()
        }
    }

    private fun fetchParkingSpaces() {
        _parkingSpaces.value = DataState.Loading

        db.collection("parkingSpaces")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    _parkingSpaces.value =
                        DataState.Error(firebaseFirestoreException.message ?: "Unknown error")
                    return@addSnapshotListener
                }

                val parkingList = mutableListOf<ModelParking>()
                for (document in querySnapshot!!) {
                    // Convert Firestore document to ModelParking object
                    val parkingSpace = document.toObject(ModelParking::class.java)
                    val docId = document.id
                    parkingSpace.docId = docId
                    parkingList.add(parkingSpace)
                }
                _parkingSpaces.value = DataState.Success(parkingList)
            }
    }
}