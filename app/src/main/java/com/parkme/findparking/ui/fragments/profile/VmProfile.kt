package com.parkme.findparking.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.parkme.findparking.data.ModelMyCar
import com.parkme.findparking.data.ModelUser
import com.parkme.findparking.preferences.PreferenceManager
import com.parkme.findparking.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VmProfile @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    private val uid = auth.currentUser?.uid!!

    private val _updateStatus = MutableLiveData<DataState<Nothing>>()
    val updateStatus: LiveData<DataState<Nothing>> = _updateStatus

    private val _getCars = MutableStateFlow<DataState<List<ModelMyCar>>>(DataState.Loading)
    val getCars: StateFlow<DataState<List<ModelMyCar>>> = _getCars

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getCarsFromDb()
        }
    }

    suspend fun updateUserProfile(user: ModelUser) {
        withContext(Dispatchers.Main) { _updateStatus.value = DataState.Loading }

        db.collection("users").document(uid).set(user)
            .addOnSuccessListener {
                preferenceManager.saveUserData(user)
                _updateStatus.value = DataState.Success()
            }
            .addOnFailureListener {
                _updateStatus.value = DataState.Error(it.message!!)
            }
    }

    suspend fun addCar(car: ModelMyCar, onComplete: (String) -> Unit) {
        _getCars.emit(DataState.Loading)
        db.collection("usersCars").document(uid).collection("cars").add(car)
            .addOnFailureListener {
                onComplete.invoke(it.message!!)
            }
    }

    private suspend fun getCarsFromDb() {
        db.collection("usersCars").document(uid).collection("cars")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    viewModelScope.launch { _getCars.emit(DataState.Error(error.message!!)) }
                    return@addSnapshotListener
                }

                val cars = mutableListOf<ModelMyCar>()
                for (document in snapshot!!.documents) {
                    val car = document.toObject(ModelMyCar::class.java)
                    val documentId = document.id
                    if (car != null) {
                        car.docId = documentId
                        cars.add(car)
                    }
                }

                viewModelScope.launch { _getCars.emit(DataState.Success(cars)) }
            }
    }

}