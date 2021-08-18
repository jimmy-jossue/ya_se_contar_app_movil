package com.janus.aprendiendonumeros.ui.fragment.auth.signup

import android.view.View
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.AESCrypt
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.databinding.FragmentSignUpPasswordAdultBinding
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.listener.ControllablePager
import com.janus.aprendiendonumeros.ui.listener.ControllablePagerObserver
import com.janus.aprendiendonumeros.ui.utilities.Constant
import com.janus.aprendiendonumeros.ui.utilities.fieldIsEmpty
import com.janus.aprendiendonumeros.ui.utilities.isLessThan

class SignUpPasswordAdultFragment : BaseFragment(R.layout.fragment_sign_up_password_adult),
    ControllablePagerObserver {

    private lateinit var binding: FragmentSignUpPasswordAdultBinding
    private lateinit var controllablePager: ControllablePager

    override fun initUI(view: View) {
        binding = FragmentSignUpPasswordAdultBinding.bind(view)
        addEvents()
    }

    private fun addEvents() {
        binding.btnBackward.setOnClickListener { controllablePager.backward() }
        binding.btnForward.setOnClickListener { validateCredentials() }
    }

    private fun validateCredentials() {
        val password: String = binding.etPassword.text.toString().trim()
        val confirmPassword: String = binding.etConfirmPassword.text.toString().trim()
        when {
            password.isEmpty() -> binding.etPassword.fieldIsEmpty(mContext,
                "la contraseña para el adulto a cargo de la cuenta")
            password.length.isLessThan(Constant.USER_PASSWORD_ADULT_LENGTH) -> binding.etPassword.error =
                "La contraseña debe tener mas de ${Constant.USER_PASSWORD_ADULT_LENGTH} caracteres."
            confirmPassword != password -> passwordsNotMatch()
            else -> saveData(password)
        }
    }

    private fun passwordsNotMatch() {
        binding.etConfirmPassword.text.clear()
        binding.etConfirmPassword.error = "Las contraseñas no coinciden. Vuelve a intentarlo"
    }

    private fun saveData(passwordAdult: String) {
        val passwordEncrypt: String = AESCrypt.encrypt(passwordAdult)
        User.staticInstance.passwordAdult = passwordEncrypt
        controllablePager.forward()
    }

    override fun setControllablePager(controllablePager: ControllablePager) {
        this.controllablePager = controllablePager
    }
}