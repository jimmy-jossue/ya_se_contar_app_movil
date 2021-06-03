package com.janus.aprendiendonumeros.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.FragmentOpeningBinding


class OpeningFragment : Fragment(R.layout.fragment_opening) {

    private lateinit var binding: FragmentOpeningBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOpeningBinding.bind(view)

        startAnimation(binding.ivAirPlane, R.anim.translate_airplane)
        startAnimation(binding.ivTitle, R.anim.bounce_in)
        startAnimation(binding.ivCloudFront, R.anim.floating_cloud_front)
        startAnimation(binding.ivCloudMiddle, R.anim.floating_cloud_middle)
        startAnimation(binding.ivCloudBack, R.anim.floating_cloud_back)

        binding.btnPlay.setOnClickListener { findNavController().navigate(R.id.action_opening_to_menu) }

        binding.ivAirPlane.setOnClickListener { startAnimation(it, R.anim.animate_airplane) }
        binding.ivTitle.setOnClickListener { startAnimation(it, R.anim.bounce_in) }
    }

    private fun getTokenFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) return@OnCompleteListener

            val token = task.result
            Toast.makeText(requireContext(), token, Toast.LENGTH_SHORT).show()
            print("Token \n \n \n $token \n \n \n")
        })
    }

    private fun startAnimation(view: View, idAnimation: Int) {
        val anim: Animation =
            AnimationUtils.loadAnimation(requireContext(), idAnimation)
        view.startAnimation(anim)
    }
}