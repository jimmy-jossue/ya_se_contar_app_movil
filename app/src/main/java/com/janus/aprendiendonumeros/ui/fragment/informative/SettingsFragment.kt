package com.janus.aprendiendonumeros.ui.fragment.informative

import android.view.View
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.FragmentSettingsBinding
import com.janus.aprendiendonumeros.ui.base.BaseFragment

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    override fun initUI(view: View) {
        binding = FragmentSettingsBinding.bind(view)

    }

}