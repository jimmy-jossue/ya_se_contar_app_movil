package com.janus.aprendiendonumeros.ui.dialog

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.PermissionRequester
import com.janus.aprendiendonumeros.databinding.DialogProfileImageBinding
import com.janus.aprendiendonumeros.ui.adapter.ImageDialogAdapter
import com.janus.aprendiendonumeros.ui.utilities.Constants

class ProfileImageDialog : DialogFragment(R.layout.dialog_profile_image) {

    private lateinit var binding: DialogProfileImageBinding
    private val rowCont: Int = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false

        binding = DialogProfileImageBinding.bind(view)
        binding.btnClose.setOnClickListener { dismiss() }
        binding.btnCamera.setOnClickListener { checkPermission() }

        val layoutManager = GridLayoutManager(context, rowCont, GridLayoutManager.HORIZONTAL, false)
        binding.rvDefaultProfilePictures.adapter = ImageDialogAdapter(getImages())
        binding.rvDefaultProfilePictures.layoutManager = layoutManager
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

    private fun openCamera() {
        //Change for registerForActivityResult
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, Constants.CODE_REQUEST_CAMERA)
        } catch (e: ActivityNotFoundException) {
            showDialogInformation()
        }
    }

    private fun checkPermission() {
        val permissionCamera: PermissionRequester =
            PermissionRequester(
                activity as ComponentActivity,
                Manifest.permission.CAMERA,
                onRationale = { showRationaleCameraUI() },
                onDenied = { showDeniedPermissionCameraUI() }
            )

        permissionCamera.runWithPermissionGranted {
            openCamera()
        }
    }

    private fun showRationaleCameraUI() {
        Toast.makeText(context, "SHOW RATIONALE CAMERA UI", Toast.LENGTH_SHORT).show()
    }

    private fun showDeniedPermissionCameraUI() {
        Toast.makeText(context, "SHOW DENIED PERMISSION CAMERA UI", Toast.LENGTH_SHORT).show()
    }

    private fun showDialogInformation() {
        Toast.makeText(context, "NO HAY APLICACION QUE PUEDA USAR LA CAMARA", Toast.LENGTH_SHORT)
            .show()
    }


}