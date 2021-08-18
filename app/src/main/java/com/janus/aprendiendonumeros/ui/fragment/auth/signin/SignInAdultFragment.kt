package com.janus.aprendiendonumeros.ui.fragment.auth.signin

import android.view.View
import androidx.fragment.app.viewModels
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.AESCrypt
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.remote.AuthDataSource
import com.janus.aprendiendonumeros.databinding.FragmentSignInAdultBinding
import com.janus.aprendiendonumeros.presentation.AuthViewModel
import com.janus.aprendiendonumeros.presentation.AuthViewModelFactory
import com.janus.aprendiendonumeros.repository.auth.AuthImpl
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.dialog.InformationDialog
import com.janus.aprendiendonumeros.ui.utilities.fieldIsEmpty
import com.janus.aprendiendonumeros.ui.utilities.goTo

class SignInAdultFragment : BaseFragment(R.layout.fragment_sign_in_adult) {

    private lateinit var binding: FragmentSignInAdultBinding
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthImpl(
                AuthDataSource()
            )
        )
    }

    override fun initUI(view: View) {
        binding = FragmentSignInAdultBinding.bind(view)
        addEvents()
    }

    private fun addEvents() {
        binding.btnSignUp.setOnClickListener { validateAge() }
        binding.btnSignIn.setOnClickListener { validateCredentials() }
    }

    private fun validateAge() {
        if (true) {
            goTo(R.id.action_signIn_to_signUp)
        } else {

        }
    }

    private fun validateCredentials() {
        val nickName: String = binding.etNickName.text.toString().trim()
        val password: String = binding.etPassword.text.toString().trim()
        when {
            nickName.isEmpty() -> binding.etNickName.fieldIsEmpty(mContext, "un nombre de usuario")
            password.isEmpty() -> binding.etPassword.fieldIsEmpty(mContext, "la contraseña")
            else -> signIn(nickName, password)
        }
    }

    private fun signIn(nickName: String, passwordAdult: String) {
        val passwordEncrypt: String = AESCrypt.encrypt(passwordAdult)

        viewModel.signInAdult(nickName, passwordEncrypt).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> binding.containerProgressBar.progressBar.visibility =
                    View.VISIBLE
                is Resource.Success -> resultSuccess(result.data)
                is Resource.Failure -> resultFailure()
            }
        })
    }

    private fun resultSuccess(idUser: String) {
        binding.containerProgressBar.progressBar.visibility = View.GONE
        mContext.showDialogInformation(
            icon = InformationDialog.ICON_SUCCESS,
            title = "Inicio de sesión exitoso",
            text = "¡BIENVENIDO!"
        )
        val action = SignInFragmentDirections.actionSignInToMenu(idUser)
        goTo(action)
    }

    private fun resultFailure() {
        binding.containerProgressBar.progressBar.visibility = View.GONE
        mContext.showDialogInformation(
            icon = InformationDialog.ICON_ERROR,
            title = "¡Ups! Inicio de sesión fallido",
            text = "Ningun usuario corresponde a los datos ingresados."
        )
    }
}