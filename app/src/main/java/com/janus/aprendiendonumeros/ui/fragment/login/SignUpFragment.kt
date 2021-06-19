package com.janus.aprendiendonumeros.ui.fragment.login

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.children
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.FragmentSignUpBinding
import com.janus.aprendiendonumeros.ui.BaseActivity
import com.janus.aprendiendonumeros.ui.utilities.Constants
import com.janus.aprendiendonumeros.ui.utilities.UIAnimations

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var mContext: BaseActivity
    private lateinit var binding: FragmentSignUpBinding
    private val imagesPassword: MutableList<ImageView> = mutableListOf()
    private val anim: UIAnimations by lazy { UIAnimations(requireContext()) }
    private var index: Int = 0
    private val PASSWORD_LENGTH: Int = 4
    private val STATUS_SELECTED: String = "selected"
    private val STATUS_DESELECTED: String = "deselected"
    private val containers: List<ConstraintLayout> by lazy {
        listOf(
            binding.containerGender,
            binding.containerBirthDate,
            binding.containerDataUser,
            binding.containerPassword,
            binding.containerRegisterSummary
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)

        binding.btnNext.setOnClickListener { nextContainer() }
        binding.btnPrevious.setOnClickListener { previousContainer() }

        addEventsGender()
        addEventsDataUser()
        addEventImagesPassword()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as BaseActivity
    }

    private fun nextContainer() {
        if (index < containers.size - 1) {
            containers[index].visibility = View.GONE
            containers[index + 1].visibility = View.VISIBLE
            index++
            binding.tvTitle.text = containers[index].tag.toString()
        }
        if (containers[index] == binding.containerRegisterSummary) {
            binding.ivSelectedProfileImage.setImageDrawable(binding.ivProfileImage.drawable)
            binding.tvSelectedBirthDate.text =
                binding.dpDate.toString()// "${binding.dpDate.dayOfMonth}/${binding.dpDate.month.}/${binding.dpDate.year}"
            binding.tvSelectedNickName.text = binding.etNickName.text

            val ivViews: List<ImageView> = listOf(
                binding.ivFirstImagePassword,
                binding.ivSecondImagePassword,
                binding.ivThirdImagePassword,
                binding.ivQuarterImagePassword
            )

            for (i in 0 until imagesPassword.size) {
                ivViews[i].setImageDrawable(imagesPassword[i].drawable)
            }


        }
        if (binding.btnPrevious.visibility == View.INVISIBLE) {
            binding.btnPrevious.visibility = View.VISIBLE
        }
    }

    private fun previousContainer() {
        if (index > 0) {
            containers[index].visibility = View.GONE
            containers[index - 1].visibility = View.VISIBLE
            index--
            binding.tvTitle.text = containers[index].tag.toString()
        }
        if (containers[index] == binding.containerGender) {
            binding.btnPrevious.visibility = View.INVISIBLE
        }

    }

    private fun addEventImagesPassword() {
        val allImagesPassword = binding.containerImagesPassword.children.iterator()
        allImagesPassword.forEach { view ->
            view.setOnClickListener { selectImageForPassword((it as ImageView)) }
        }
    }

    private fun addEventsGender() {
        binding.ivGenderBoy.setOnClickListener { selectGender(it.id) }
        binding.ivGenderGirl.setOnClickListener { selectGender(it.id) }
    }

    private fun selectGender(id: Int) {
        if (id == binding.ivGenderBoy.id) {
            binding.ivGenderBoy.setImageResource(R.drawable.ic_photo_profile_boy_selected)
            binding.tvGenderBoy.setTextColor(getColor(requireContext(), R.color.purple_700))
            binding.ivGenderGirl.setImageResource(R.drawable.ic_photo_profile_girl_deselected)
            binding.tvGenderGirl.setTextColor(getColor(requireContext(), R.color.gray))
            binding.ivProfileImage.setImageResource(R.drawable.ic_photo_profile_boy_deselected)
            binding.tvSelectedGender.text = binding.tvGenderBoy.text
        } else {
            binding.ivGenderBoy.setImageResource(R.drawable.ic_photo_profile_boy_deselected)
            binding.tvGenderBoy.setTextColor(getColor(requireContext(), R.color.gray))
            binding.ivGenderGirl.setImageResource(R.drawable.ic_photo_profile_girl_selected)
            binding.tvGenderGirl.setTextColor(getColor(requireContext(), R.color.purple_700))
            binding.tvSelectedGender.text = binding.tvGenderGirl.text
            binding.ivProfileImage.setImageResource(R.drawable.ic_photo_profile_girl_deselected)
        }
    }

    private fun addEventsDataUser() {
        binding.btnAddImageProfile.setOnClickListener {
            mContext.openDialogProfileImage()
        }
    }

    private fun selectImageForPassword(view: ImageView) {
        val tag: String = view.tag.toString()
        if (tag == STATUS_DESELECTED) {
            if (imagesPassword.size < PASSWORD_LENGTH) {
                view.tag = STATUS_SELECTED
                view.setBackgroundResource(R.drawable.ic_mark_selection)
                view.setPadding(0)
                imagesPassword.add(view)
            } else imagesPassword.forEach { anim.startSimple(it, R.anim.attention) }
        } else {
            view.tag = STATUS_DESELECTED
            view.setBackgroundResource(0)
            view.setPadding(resources.getDimension(R.dimen.separation_extra_small).toInt())
            imagesPassword.remove(view)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.CODE_REQUEST_CAMERA && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.ivProfileImage.setImageBitmap(imageBitmap)
        }
    }
}