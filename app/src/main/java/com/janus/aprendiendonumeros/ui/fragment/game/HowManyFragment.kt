package com.janus.aprendiendonumeros.ui.fragment.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.FragmentHowManyBinding

class HowManyFragment : Fragment(R.layout.fragment_how_many) {

    private lateinit var binding: FragmentHowManyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHowManyBinding.bind(view)


    }
}