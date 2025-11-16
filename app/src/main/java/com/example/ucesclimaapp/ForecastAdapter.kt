package com.example.ucesclimaapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ucesclimaapp.databinding.ItemForecastBinding
import com.example.ucesclimaapp.model.DailyForecast

class ForecastAdapter(
    private var list: List<DailyForecast>
) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    inner class ForecastViewHolder(val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ItemForecastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val item = list[position]

        holder.binding.tvDate.text = item.date
        holder.binding.tvTemp.text =
            "Temp promedio: ${item.tempAvg?.let { "%.1f°C".format(it) } ?: "---"}"
        holder.binding.tvRain.text =
            "Lluvia promedio: ${item.rainAvg?.let { "%.1f mm".format(it) } ?: "---"}"
    }

    override fun getItemCount() = list.size

    fun updateData(newList: List<DailyForecast>) {
        list = newList
        notifyDataSetChanged()
    }
}
