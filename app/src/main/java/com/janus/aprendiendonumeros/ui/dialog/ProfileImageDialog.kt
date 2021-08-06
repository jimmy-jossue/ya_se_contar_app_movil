package com.janus.aprendiendonumeros.ui.dialog

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.DialogProfileImageBinding
import com.janus.aprendiendonumeros.ui.adapter.ImageDialogAdapter
import com.janus.aprendiendonumeros.ui.listener.ItemGalleryListener

class ProfileImageDialog : DialogFragment(R.layout.dialog_profile_image), ItemGalleryListener {

    private lateinit var binding: DialogProfileImageBinding
    private val rowCont: Int = 2
    private lateinit var drawable: Drawable
    private lateinit var listener: Listener

    interface Listener {
        fun onSelectionImage(dialog: ProfileImageDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false

        binding = DialogProfileImageBinding.bind(view)
        binding.btnClose.setOnClickListener { dismiss() }
        //binding.btnCamera.setOnClickListener { checkPermission() }

        val layoutManager = GridLayoutManager(context, rowCont, GridLayoutManager.HORIZONTAL, false)
        val adapter = ImageDialogAdapter(getImages())
        adapter.setListener(this)
        binding.rvDefaultProfilePictures.adapter = adapter
        binding.rvDefaultProfilePictures.layoutManager = layoutManager

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as Listener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement Listener")
            )
        }
    }

    private fun getImages(): List<Drawable> {
        val drawableList = mutableListOf<Drawable>()
        val typeArray: TypedArray = resources.obtainTypedArray(R.array.default_user_profile_images)
        val length: Int = typeArray.length() - 1
        for (item: Int in 0..length) {
            typeArray.getDrawable(item)?.let {
                drawableList.add(it)
            }
        }
        typeArray.recycle()
        return drawableList
    }

    private fun showDialogInformation() {
        Toast.makeText(context, "NO HAY APLICACION QUE PUEDA USAR LA CAMARA", Toast.LENGTH_SHORT)
            .show()
    }

    override fun OnItemSelect(view: AppCompatImageView) {
        view.scaleX = 1.1F
        view.scaleY = 1.1F
        drawable = view.drawable
        listener.onSelectionImage(this)
    }

    fun getDrawable(): Drawable {
        return drawable
    }
}