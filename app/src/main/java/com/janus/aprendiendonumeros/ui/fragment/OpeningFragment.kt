package com.janus.aprendiendonumeros.ui.fragment

import android.animation.ValueAnimator
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.FragmentOpeningBinding


class OpeningFragment : Fragment(R.layout.fragment_opening) {

    private lateinit var binding: FragmentOpeningBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOpeningBinding.bind(view)

        binding.ivAirPlane.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.translate_airplane
            )
        )

        binding.ivTitle.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_in))
        binding.ivCloudFront.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.floating_cloud_front))
        binding.ivCloudMiddle.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.floating_cloud_middle))
        binding.ivCloudBack.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.floating_cloud_back))

        binding.btnPlay.setOnClickListener {
            it.startAnimation(
                AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_in )
            ).run { findNavController().navigate(R.id.action_opening_to_menu) }
        }

        binding.ivAirPlane.setOnClickListener {
            val anim: Animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.animate_airplane)
            it.startAnimation(anim)

        }

        binding.ivTitle.setOnClickListener {
            val anim: Animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_in)
            it.startAnimation(anim)
        }
    }
}

