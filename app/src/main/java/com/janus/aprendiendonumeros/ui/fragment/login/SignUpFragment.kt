package com.janus.aprendiendonumeros.ui.fragment.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding

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

//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding = null
//    }

}