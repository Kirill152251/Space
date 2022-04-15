package com.example.space.ui.map_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.space.R
import com.example.space.databinding.ItemMarkerBinding

class MarkersAdapter(private val deleteMarker: (position: Int) -> Unit) :
    ListAdapter<MapMarker, MarkersAdapter.MarkersViewHolder>(TaskDiffCallBack()) {


    class MarkersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMarkerBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_marker, parent, false)
        return MarkersViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarkersViewHolder, position: Int) {
        val marker = getItem(position)
        holder.binding.apply {
            textMarkerName.text = marker.name
            textLatValue.text = marker.latLng.latitude.toString()
            textLonValue.text = marker.latLng.longitude.toString()
            imageDeleteMarker.setOnClickListener {
                deleteMarker(position)
            }
        }
    }



    class TaskDiffCallBack : DiffUtil.ItemCallback<MapMarker>() {
        override fun areItemsTheSame(oldItem: MapMarker, newItem: MapMarker): Boolean {
            return oldItem.latLng.latitude == newItem.latLng.latitude
        }

        override fun areContentsTheSame(oldItem: MapMarker, newItem: MapMarker): Boolean =
            oldItem == newItem
    }
}
