package com.parkme.findparking.ui.fragments.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.parkme.findparking.data.ModelParking
import com.parkme.findparking.databinding.ItemParkingBinding
import com.parkme.findparking.utils.gone
import com.parkme.findparking.utils.wrapWithRsHr
import com.parkme.findparking.utils.wrapWithSpots
import java.util.Locale

class AdapterParking(
    private var items: List<ModelParking>,
    val spaceCallback: (ModelParking) -> Unit
) :
    RecyclerView.Adapter<AdapterParking.ViewHolder>() {
    private var filteredList: List<ModelParking> = items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemParkingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredList[position]
        holder.bind(item)
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint.toString().lowercase(Locale.getDefault())
                val filtered = if (query.isEmpty()) {
                    items
                } else {
                    items.filter { parking ->
                        parking.spaceName.lowercase(Locale.getDefault()).contains(query) ||
                                parking.spaceLocationInText.lowercase(Locale.getDefault())
                                    .contains(query) ||
                                parking.pricePerHour.lowercase(Locale.getDefault())
                                    .contains(query) ||
                                parking.numberOfSpots.toString().contains(query)
                    }
                }
                val results = FilterResults()
                results.values = filtered
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                filteredList = results?.values as List<ModelParking>
                notifyDataSetChanged()
            }
        }
    }

    fun updateData(newData: List<ModelParking>) {
        items = newData
        filteredList = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemParkingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ModelParking) {
            Glide.with(itemView.context)
                .load(data.spaceImage)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.pgImageParking.gone()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable?>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.pgImageParking.gone()
                        return false
                    }
                })
                .into(binding.imgParkingArea)
            binding.tvNameParkingArea.text = data.spaceName
            binding.tvLocation.text = data.spaceLocationInText
            binding.tvPrice.text = data.pricePerHour.wrapWithRsHr()
            binding.tvNumOfSpots.text = data.numberOfSpots.toString().wrapWithSpots()

            itemView.setOnClickListener {
                spaceCallback.invoke(data)
            }
        }
    }

}