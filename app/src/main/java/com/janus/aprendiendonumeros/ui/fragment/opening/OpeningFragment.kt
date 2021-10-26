package com.janus.aprendiendonumeros.ui.fragment.opening

import android.view.View
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.data.model.SettingsSession
import com.janus.aprendiendonumeros.databinding.FragmentOpeningBinding
import com.janus.aprendiendonumeros.ui.animation.UIAnimations
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.utilities.Sound
import com.janus.aprendiendonumeros.ui.utilities.goTo

class OpeningFragment : BaseFragment(R.layout.fragment_opening) {

    private lateinit var binding: FragmentOpeningBinding
    private val anim: UIAnimations by lazy { UIAnimations(requireContext()) }
    private val soundBackground: Sound by lazy { Sound(mContext) }
    //Sound("https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/general_sounds%2Ffinish.mp3?alt=media&token=6c93ef60-e5ee-4c2e-b035-d0c100b62013")

    override fun initUI(view: View) {
        binding = FragmentOpeningBinding.bind(view)
        setUpData()
        setUpEvents()
        setUpAnimations()
    }

    private fun setUpData() {
        soundBackground.setUrl("https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/general_sounds%2Ffinish.mp3?alt=media&token=6c93ef60-e5ee-4c2e-b035-d0c100b62013")
    }

    private fun setUpEvents() {
        binding.ivAirPlane.setOnClickListener {
            anim.startAnimation(it, R.anim.animate_airplane)
        }
        binding.ivTitle.setOnClickListener {
            anim.startAnimation(it, R.anim.bounce_in)
        }
        binding.btnPlay.setOnClickListener {
            goTo(R.id.action_opening_to_signIn)
        }
    }

    private fun setUpAnimations() {
        binding.root.post {
            anim.startAnimation(binding.ivAirPlane, R.anim.translate_airplane)
            anim.startAnimation(binding.ivTitle, R.anim.bounce_in)
            anim.startAnimation(binding.ivCloudFront, R.anim.floating_cloud_front)
            anim.startAnimation(binding.ivCloudMiddle, R.anim.floating_cloud_middle)
            anim.startAnimation(binding.ivCloudBack, R.anim.floating_cloud_back)
            soundBackground.play()
        }
    }

    private fun isRegistered(session: SettingsSession) {
        if (session.saved) {
            val action = OpeningFragmentDirections.actionOpeningToMenu(session.id)
            goTo(action)
        } else {
            goTo(R.id.action_opening_to_signIn)
        }
    }
}