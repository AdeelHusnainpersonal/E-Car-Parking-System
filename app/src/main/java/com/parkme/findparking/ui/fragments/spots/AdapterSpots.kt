package com.parkme.findparking.ui.fragments.spots

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.parkme.findparking.R
import com.parkme.findparking.data.ModelSpots
import com.parkme.findparking.databinding.ItemSpotBinding

class AdapterSpots(private val items: List<ModelSpots>, val spotNameCallBack: (String) -> Unit) :
    RecyclerView.Adapter<AdapterSpots.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSpotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.bind(data)
    }

    inner class ViewHolder(val binding: ItemSpotBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ModelSpots) {
            if (data.booked) {
                binding.spotCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.color_spot_booked
                    )
                )
            } else {
                binding.spotCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.color_spot_unbooked
                    )
                )
            }

            binding.tvSpotName.text = data.spotName

            itemView.setOnClickListener {
                if (!data.booked) {
                    spotNameCallBack.invoke(data.spotName)
                } else {
                    Toast.makeText(itemView.context, "Already Booked", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}