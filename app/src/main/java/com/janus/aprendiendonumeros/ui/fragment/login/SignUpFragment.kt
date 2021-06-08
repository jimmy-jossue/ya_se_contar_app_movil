package com.janus.aprendiendonumeros.ui.fragment.login

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.databinding.FragmentSignUpBinding
import com.janus.aprendiendonumeros.ui.utilities.UIAnimations

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding
    private val imagesPassword: MutableList<View> = mutableListOf()
    private val anim: UIAnimations by lazy { UIAnimations(requireContext()) }
    private var index: Int = 0
    private val user: User = User()
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
        addEventImagesPassword()
    }

    private fun nextContainer() {
        if (index < containers.size - 1) {
            containers[index].visibility = View.GONE
            containers[index + 1].visibility = View.VISIBLE
            index++
            binding.tvTitle.text = containers[index].tag.toString()
        }
    }

    private fun previousContainer() {
        if (index > 0) {
            containers[index].visibility = View.GONE
            containers[index - 1].visibility = View.VISIBLE
            index--
            binding.tvTitle.text = containers[index].tag.toString()
        }
    }


    private fun addEventImagesPassword() {
        val allImagesPassword = binding.containerImagesPassword.children.iterator()
        allImagesPassword.forEach { view ->
            view.setOnClickListener { selectImageForPassword(it) }
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
            user.gender = binding.tvGenderBoy.text.toString()
        } else {
            binding.ivGenderBoy.setImageResource(R.drawable.ic_photo_profile_boy_deselected)
            binding.tvGenderBoy.setTextColor(getColor(requireContext(), R.color.gray))
            binding.ivGenderGirl.setImageResource(R.drawable.ic_photo_profile_girl_selected)
            binding.tvGenderGirl.setTextColor(getColor(requireContext(), R.color.purple_700))
            user.gender = binding.tvGenderGirl.text.toString()
        }
    }

    private fun addEventsDataUser() {
        binding.btnAddImageProfile.setOnClickListener { /*Abrir modal*/ }
    }


    private fun selectImageForPassword(view: View) {
        val tag: String = view.tag.toString()
        if (tag == "deselected") {
            if (imagesPassword.size < 3) {
                view.tag = "selected"
                view.setBackgroundResource(R.drawable.ic_mark_selection)
                imagesPassword.add(view)
            } else imagesPassword.forEach { anim.startSimple(it, R.anim.attention) }
        } else {
            view.tag = "deselected"
            view.setBackgroundResource(0)
            imagesPassword.remove(view)
        }
    }
}