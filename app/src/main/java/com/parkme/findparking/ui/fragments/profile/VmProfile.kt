package com.parkme.findparking.ui.fragments.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.parkme.findparking.data.ModelMyCar
import com.parkme.findparking.data.ModelUser
import com.parkme.findparking.preferences.PreferenceManager
import com.parkme.findparking.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private var listenerRegistration: ListenerRegistration? = null

    private val _updateStatus = MutableLiveData<DataState<Nothing>>()
    val updateStatus: LiveData<DataState<Nothing>>
        get() = _updateStatus

    private val _carStatus = MutableLiveData<DataState<List<ModelMyCar>>>()
    val getCars: LiveData<DataState<List<ModelMyCar>>>
        get() = _carStatus

    init {
        viewModelScope.launch(Dispatchers.IO){
            getCarsFromDb()
        }
    }

    suspend fun updateUserProfile(user: ModelUser){
        withContext(Dispatchers.Main){ _updateStatus.value = DataState.Loading }

        db.collection("users").document(uid).set(user)
            .addOnSuccessListener {
                preferenceManager.saveUserData(user)
                _updateStatus.value = DataState.Success()
            }
            .addOnFailureListener {
                _updateStatus.value = DataState.Error(it.message!!)
            }
    }

    suspend fun addCar(car: ModelMyCar, onComplete:(String) -> Unit){
        withContext(Dispatchers.Main){ _carStatus.value = DataState.Loading }
        db.collection("usersCars").document(uid).collection("cars").add(car)
            .addOnFailureListener {
                onComplete.invoke(it.message!!)
            }
    }

    private fun getCarsFromDb(){
        val query = db.collection("usersCars").document(uid).collection("cars")

        listenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.d("Firestore", "Listen failed.", error)
                _carStatus.value = DataState.Error(error.message!!)
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

            _carStatus.value = DataState.Success(cars)
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
        listenerRegistration = null
    }
}