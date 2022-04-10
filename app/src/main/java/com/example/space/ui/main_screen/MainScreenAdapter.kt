package com.example.space.ui.main_screen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.api_response_model.Photo
import com.example.space.R
import com.example.space.databinding.PhotoItemBinding
import com.example.space.ui.main_screen.MainScreenAdapter.PhotosViewHolder
import com.example.space.utils.parseImageUrl

class MainScreenAdapter(private val clickListener: (photoItem: Photo) -> Unit) :
    PagingDataAdapter<Photo, PhotosViewHolder>(PHOTO_COMPARATOR) {

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo!!)
        holder.itemView.setOnClickListener { clickListener(photo) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return PhotosViewHolder(view)
    }

    class PhotosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = PhotoItemBinding.bind(view)
        fun bind(photo: Photo) {
            binding.apply {
                textCameraName.text = photo.camera.fullName
                textRoverName.text = photo.rover.name
                imagePhoto.load(photo.image.parseImageUrl()) {
                    crossfade(true)
                    crossfade(300)
                }
            }
        }
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem.image == newItem.image

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem == newItem
        }
    }
}