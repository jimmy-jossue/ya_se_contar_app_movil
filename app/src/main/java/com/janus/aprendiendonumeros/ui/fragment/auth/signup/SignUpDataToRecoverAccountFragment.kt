package com.janus.aprendiendonumeros.ui.fragment.auth.signup

import android.util.Patterns
import android.view.View
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.databinding.FragmentSignUpDataToRecoverAccountBinding
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.listener.ControllablePager
import com.janus.aprendiendonumeros.ui.listener.ControllablePagerObserver

class SignUpDataToRecoverAccountFragment :
    BaseFragment(R.layout.fragment_sign_up_data_to_recover_account),
    ControllablePagerObserver {

    private lateinit var binding: FragmentSignUpDataToRecoverAccountBinding
    private lateinit var controllablePager: ControllablePager

    override fun initUI(view: View) {
        binding = FragmentSignUpDataToRecoverAccountBinding.bind(view)
        addEvents()
    }

    private fun addEvents() {
        binding.btnBackward.setOnClickListener { controllablePager.backward() }
        binding.btnFinish.setOnClickListener { validateCredentials() }
    }

    private fun validateCredentials() {
        val email: String = binding.etEmail.text.toString().trim()
        when {
            email.isNotEmpty() -> validateEmail()
            else -> saveData()
        }
    }

    private fun validateEmail() {
        val pattern = Patterns.EMAIL_ADDRESS
        val email: String = binding.etEmail.text.toString().trim()
        val validateEmail: Boolean = pattern.matcher(email).matches()
        if (validateEmail) {
            saveData()
        } else {
            binding.etEmail.error = "El formato de la contraseña no es correcto."
        }
    }

    private fun saveData() {
        val email: String = binding.etEmail.text.toString().trim()
        if (email.isNotEmpty()) {
            User.staticInstance.email = email
        }
        controllablePager.finish()
    }

    override fun setControllablePager(controllablePager: ControllablePager) {
        this.controllablePager = controllablePager
    }
}