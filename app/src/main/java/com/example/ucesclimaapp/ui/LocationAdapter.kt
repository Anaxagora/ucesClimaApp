package com.example.ucesclimaapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ucesclimaapp.databinding.ItemLocationBinding
import com.example.ucesclimaapp.model.Location

class LocationAdapter(
    private var locations: List<Location>,
    private val onItemClick: (Location) -> Unit,
    private val onDeleteClick: (Location) -> Unit
) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Location) {
            binding.tvCity.text = item.city
            binding.tvLat.text = item.lat.toString()
            binding.tvLon.text = item.lon.toString()

            binding.root.setOnClickListener { onItemClick(item) }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLocationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(locations[position])
    }

    override fun getItemCount(): Int = locations.size

    fun updateList(newList: List<Location>) {
        locations = newList
        notifyDataSetChanged()
    }
}

