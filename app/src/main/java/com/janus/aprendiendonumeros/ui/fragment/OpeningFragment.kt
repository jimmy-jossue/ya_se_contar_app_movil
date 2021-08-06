package com.janus.aprendiendonumeros.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.datastore.SettingsSessionDataStore
import com.janus.aprendiendonumeros.data.model.SettingsSession
import com.janus.aprendiendonumeros.databinding.FragmentOpeningBinding
import com.janus.aprendiendonumeros.presentation.SettingsSessionViewModel
import com.janus.aprendiendonumeros.presentation.SettingsSessionViewModelFactory
import com.janus.aprendiendonumeros.repository.settingssession.SettingsSessionImpl
import com.janus.aprendiendonumeros.ui.utilities.Sound
import com.janus.aprendiendonumeros.ui.utilities.UIAnimations
import com.janus.aprendiendonumeros.ui.utilities.goTo


class OpeningFragment : Fragment(R.layout.fragment_opening) {

    private lateinit var binding: FragmentOpeningBinding
    private val anim: UIAnimations by lazy { UIAnimations(requireContext()) }
    private lateinit var settingsSession: SettingsSession
    private val soundBackground: Sound =
        Sound("https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/general_sounds%2Ffinish.mp3?alt=media&token=6c93ef60-e5ee-4c2e-b035-d0c100b62013")
    private val viewModel by viewModels<SettingsSessionViewModel> {
        SettingsSessionViewModelFactory(
            SettingsSessionImpl(
                SettingsSessionDataStore(requireContext())
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOpeningBinding.bind(view)

        binding.root.post {
            anim.startAnimation(binding.ivAirPlane, R.anim.translate_airplane)
            anim.startAnimation(binding.ivTitle, R.anim.bounce_in)
            anim.startAnimation(binding.ivCloudFront, R.anim.floating_cloud_front)
            anim.startAnimation(binding.ivCloudMiddle, R.anim.floating_cloud_middle)
            anim.startAnimation(binding.ivCloudBack, R.anim.floating_cloud_back)
            binding.ivAirPlane.setOnClickListener {
                anim.startAnimation(
                    it,
                    R.anim.animate_airplane
                )
            }
            binding.ivTitle.setOnClickListener { anim.startAnimation(it, R.anim.bounce_in) }

            binding.btnPlay.setOnClickListener {
                viewModel.fetchSettingsSession().observe(viewLifecycleOwner, { result ->
                    when (result) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            isRegistered(result.data)
                        }
                        is Resource.Failure -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                context,
                                "Error al comoprovar la sesion\n¿te gustaria crear una cuenta?",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }

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