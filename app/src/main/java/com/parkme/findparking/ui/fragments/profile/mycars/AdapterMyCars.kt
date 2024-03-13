package com.parkme.findparking.ui.fragments.profile.mycars

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.parkme.findparking.data.ModelMyCar
import com.parkme.findparking.databinding.ItemMyCarBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class AdapterMyCars(private val items: List<ModelMyCar>) :
    RecyclerView.Adapter<AdapterMyCars.ViewHolder>() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.bind(data)
    }

    inner class ViewHolder(val binding: ItemMyCarBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ModelMyCar) {
            binding.apply {
                tvCarName.text = data.carName
                tvCarPlateNumber.text = data.carNumber
                btnDelete.setOnClickListener {
                    data.docId?.let { id -> delete(id) }
                }
            }
        }

        private fun delete(docId: String) {
            db.collection("usersCars").document(auth.currentUser!!.uid).collection("cars")
                .document(docId).delete()
                .addOnSuccessListener {
                    Toast.makeText(itemView.context,"Deleted",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(itemView.context,it.message,Toast.LENGTH_SHORT).show()
                }
        }
    }

}