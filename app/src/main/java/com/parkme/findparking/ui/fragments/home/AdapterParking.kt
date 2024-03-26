package com.parkme.findparking.ui.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parkme.findparking.data.ModelParking
import com.parkme.findparking.databinding.ItemParkingBinding

class AdapterParking(private val items: ArrayList<ModelParking>) :
    RecyclerView.Adapter<AdapterParking.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemParkingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.bind(data)
    }

    inner class ViewHolder(val binding: ItemParkingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ModelParking) {
            binding.imgParkingArea.setImageResource(data.imgParking)
            binding.tvNameParkingArea.text = data.nameParking
            binding.tvLocation.text = data.locationParking
            binding.tvPrice.text = data.priceParkingPerHour
            binding.tvNumOfSpots.text = data.numOfSpots
        }
    }

}