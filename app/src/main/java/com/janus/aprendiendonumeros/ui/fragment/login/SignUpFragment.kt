package com.janus.aprendiendonumeros.ui.fragment.login

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.FragmentSignUpBinding
import com.janus.aprendiendonumeros.ui.utilities.UIAnimations

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding
    private val imagesPassword: MutableList<View> = mutableListOf()
    private val anim: UIAnimations = UIAnimations(requireContext())


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignUpBinding.bind(view)


        binding.containerGender.animate().alpha(0F).start()
        val durationAnimation: Long = 500
        binding.btnNext.setOnClickListener {
            binding.containerDataUser.animate().alpha(0F).setDuration(durationAnimation).start()
            binding.containerGender.animate().alpha(1F).setDuration(durationAnimation)
                .setStartDelay(durationAnimation).start()
        }

        binding.btnPrevious.setOnClickListener {
            binding.containerGender.animate().alpha(0F).setDuration(durationAnimation).start()
            binding.containerDataUser.animate().alpha(1F).setDuration(durationAnimation)
                .setStartDelay(durationAnimation).start()
        }
    }


    private fun addEventImagesPassword() {
        val allImagesPassword = binding.containerImagesPassword.children.iterator()
        allImagesPassword.forEach {
            it.setOnClickListener { view ->
                selectImageForPassword(view)
            }
        }
    }

    private fun selectImageForPassword(view: View) {
        if (imagesPassword.size < 3) {
            selectImage(view)
        } else imagesPassword.forEach { anim.startSimple(it, R.anim.error) }
    }

    private fun selectImage(view: View) {
        val tag: String = view.tag.toString()
        if (tag == "deselected") {
            view.tag = "selected"
            view.setBackgroundResource(R.drawable.ic_status_complete)
            imagesPassword.add(view)
        } else {
            view.tag = "deselected"
            view.setBackgroundResource(0)
            imagesPassword.remove(view)
        }
    }
}