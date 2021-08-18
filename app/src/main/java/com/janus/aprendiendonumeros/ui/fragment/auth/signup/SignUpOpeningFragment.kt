package com.janus.aprendiendonumeros.ui.fragment.auth.signup

import android.view.View
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.FragmentSignUpOpeningBinding
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.listener.ControllablePager
import com.janus.aprendiendonumeros.ui.listener.ControllablePagerObserver

class SignUpOpeningFragment : BaseFragment(R.layout.fragment_sign_up_opening),
    ControllablePagerObserver {

    private lateinit var binding: FragmentSignUpOpeningBinding
    private lateinit var controllablePager: ControllablePager

    override fun initUI(view: View) {
        binding = FragmentSignUpOpeningBinding.bind(view)
        addEvents()
    }

    override fun setControllablePager(controllablePager: ControllablePager) {
        this.controllablePager = controllablePager
    }

    private fun addEvents() {
        binding.btnForward.setOnClickListener { controllablePager.forward() }
    }
}