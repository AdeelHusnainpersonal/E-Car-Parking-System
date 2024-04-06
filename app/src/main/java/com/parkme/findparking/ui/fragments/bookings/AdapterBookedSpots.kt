package com.parkme.findparking.ui.fragments.bookings

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.androchef.happytimer.countdowntimer.HappyTimer
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.parkme.findparking.data.ModelBookedCar
import com.parkme.findparking.databinding.ItemBookedSpotBinding

class AdapterBookedSpots(private val items: List<ModelBookedCar>) :
    RecyclerView.Adapter<AdapterBookedSpots.ViewHolder>() {
    private val db = Firebase.firestore
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemBookedSpotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.bind(data)
    }

    inner class ViewHolder(val binding: ItemBookedSpotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ModelBookedCar) {
            binding.tvParkingName.text = data.parkingSpaceName
            binding.tvCarName.text = data.carName
            binding.tvCarNumber.text = data.carNumber
            val timer = binding.normalCountDownView

            // Get the current time in seconds
            val currentTimeSeconds = System.currentTimeMillis() / 1000

            // Calculate the remaining time in seconds
            val remainingTimeSeconds = data.endTimeSeconds - currentTimeSeconds

            if (remainingTimeSeconds > 0) {
                // Initialize the timer with the remaining time
                timer.initTimer(remainingTimeSeconds.toInt())
                timer.startTimer()
            } else {
                // The timer has already ended, perform deletion process
                deletedDoc(data)
            }
            //set OnTickListener for getting updates on time. [Optional]
            timer.setOnTickListener(object : HappyTimer.OnTickListener {
                override fun onTick(completedSeconds: Int, remainingSeconds: Int) {}
                override fun onTimeUp() {
                    deletedDoc(data)
                }
            })
        }

        private fun deletedDoc(data: ModelBookedCar) {
            db.collection("bookedSpots").document(data.docId)
                .delete()
                .addOnSuccessListener {
                    updateBookedSpotToFalse(data.parkingSpaceName, data.spotName)
                }
                .addOnFailureListener {
                    Toast.makeText(itemView.context, "Error deleting document", Toast.LENGTH_SHORT)
                        .show()
                }
        }


        private fun updateBookedSpotToFalse(parkingSpaceName: String, spotName: String) {
            db.collection("parkingSpaces").document(parkingSpaceName).collection("spots")
                .document(spotName)
                .update("booked", false)
                .addOnSuccessListener {
                    Toast.makeText(itemView.context, "Deleted and updated", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(itemView.context, exception.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

}