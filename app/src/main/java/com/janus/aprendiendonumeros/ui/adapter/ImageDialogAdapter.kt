package com.janus.aprendiendonumeros.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.ItemGalleryBinding
import com.janus.aprendiendonumeros.ui.listener.ItemGalleryListener

class ImageDialogAdapter(private val dataSet: List<Drawable>) :
    RecyclerView.Adapter<ImageDialogAdapter.ViewHolder>() {

    private lateinit var listener: ItemGalleryListener

    fun setListener(listener: ItemGalleryListener) {
        this.listener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemGalleryBinding = ItemGalleryBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ivImage.setImageDrawable(dataSet[position])
        holder.binding.ivImage.setOnClickListener {
            val iv: AppCompatImageView = it as AppCompatImageView
            listener.onItemSelect(iv)
        }
    }

    override fun getItemCount(): Int = dataSet.size
}