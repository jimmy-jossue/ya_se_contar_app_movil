package com.janus.aprendiendonumeros.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.ItemGalleryBinding

class ImageDialogAdapter(private val dataSet: List<Drawable>) :
    RecyclerView.Adapter<ImageDialogAdapter.ViewHolder>() {

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
    }

    override fun getItemCount(): Int = dataSet.size
}
//class ImageDialogAdapter(private val dataSet: List<GalleryImage>): RecyclerView.Adapter<BaseViewHolder<*>>() {
//
//    private inner class ImageDialogViewHolder(
//        val binding: ItemGalleryBinding,
//        val context: Context
//    ): BaseViewHolder<GalleryImage>(binding.root){
//        override fun bind(item: GalleryImage) {
//            Glide.with(context).load(item.url).centerCrop().into(binding.ivImage)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
//        val itemBinding = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return  ImageDialogViewHolder(itemBinding, parent.context)
//    }
//
//    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
//        when(holder){
//            is ImageDialogViewHolder -> holder.bind(dataSet[position])
//        }
//    }
//
//    override fun getItemCount() = dataSet.size
//}