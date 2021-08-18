package com.janus.aprendiendonumeros.ui.fragment.auth.signup

import android.graphics.Bitmap
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.data.remote.AuthDataSource
import com.janus.aprendiendonumeros.data.remote.ImageDataSource
import com.janus.aprendiendonumeros.databinding.FragmentSignUpBinding
import com.janus.aprendiendonumeros.presentation.AuthViewModel
import com.janus.aprendiendonumeros.presentation.AuthViewModelFactory
import com.janus.aprendiendonumeros.presentation.ResourceImageViewModel
import com.janus.aprendiendonumeros.presentation.ResourceImageViewModelFactory
import com.janus.aprendiendonumeros.repository.auth.AuthImpl
import com.janus.aprendiendonumeros.repository.resourceimage.ResourceImageImpl
import com.janus.aprendiendonumeros.ui.adapter.SignUpViewPagerAdapter
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.dialog.InformationDialog
import com.janus.aprendiendonumeros.ui.listener.ControllablePager
import com.janus.aprendiendonumeros.ui.listener.ControllablePagerObserver
import com.janus.aprendiendonumeros.ui.utilities.Statics
import com.janus.aprendiendonumeros.ui.utilities.goTo
import java.util.*

class SignUpFragment : BaseFragment(R.layout.fragment_sign_up), ControllablePager {

    private lateinit var binding: FragmentSignUpBinding
    private val authViewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthImpl(
                AuthDataSource()
            )
        )
    }
    private val resourceImageViewModel by viewModels<ResourceImageViewModel> {
        ResourceImageViewModelFactory(
            ResourceImageImpl(
                ImageDataSource()
            )
        )
    }

    override fun initUI(view: View) {
        binding = FragmentSignUpBinding.bind(view)
        setUpPager()
    }

    private fun setUpPager() {
        val listFragments: List<Fragment> = listOf(
            SignUpOpeningFragment(),
            SignUpGenderFragment(),
            SignUpPasswordChildFragment(),
            SignUpBirthDateFragment(),
            SignUpDataUserFragment(),
            SignUpPasswordAdultFragment(),
            SignUpDataToRecoverAccountFragment()
        )
        val adapter = SignUpViewPagerAdapter(this)
        adapter.setListFragment(listFragments)
        binding.pager.adapter = adapter
        binding.pager.isUserInputEnabled = false

        setUpObserver(listFragments)
    }

    private fun setUpObserver(list: List<Fragment>) {
        list.forEach { fragment ->
            if (fragment is ControllablePagerObserver) {
                fragment.setControllablePager(this)
            }
        }
    }

    // ControllablePager override method
    override fun forward() {
        binding.pager.setCurrentItem(binding.pager.currentItem + 1, true)
    }

    override fun backward() {
        binding.pager.setCurrentItem(binding.pager.currentItem - 1, true)
    }

    override fun finish() {
        validateData()
    }
    //End ControllablePager override method

    private fun validateData() {
        when {
            User.staticInstance.nickName.isEmpty() -> messageYouForgotData("un nombre de usuario")
            User.staticInstance.passwordChild.isEmpty() -> messageYouForgotData("la contraseña para el inicio de sesión del niño")
            User.staticInstance.passwordAdult.isEmpty() -> messageYouForgotData("la contraseña para el inicio de sesión del adulto acargo de la cuenta")
            else -> validateProfileImage()
        }
    }

    private fun validateProfileImage() {
        val userId: String = UUID.randomUUID().toString()
        saveProfileImage(Statics.profileImageBitmap, userId)
    }

    private fun saveProfileImage(bitmap: Bitmap, userId: String) {
        resourceImageViewModel.saveProfileImage(bitmap, userId)
            .observe(viewLifecycleOwner, { result ->
                when (result) {
                    is Resource.Loading -> binding.containerProgressBar.progressBar.visibility =
                        View.VISIBLE
                    is Resource.Success -> {
                        User.staticInstance.image = result.data
                        signUp(userId, User.staticInstance)
                    }
                    is Resource.Failure -> resultFailure(result.exception)
                }
            })
    }

    private fun signUp(userId: String, user: User) {
        authViewModel.signUp(userId, user).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> binding.containerProgressBar.progressBar.visibility =
                    View.VISIBLE
                is Resource.Success -> resultSuccess()
                is Resource.Failure -> resultFailure(result.exception)
            }
        })
    }

    private fun resultSuccess() {
        mContext.showDialogInformation(
            icon = InformationDialog.ICON_SUCCESS,
            title = "¡Genial!",
            text = "La cuenta de usuario se creo correctamente."
        )
        goTo(R.id.action_signUp_to_signIn)
    }

    private fun resultFailure(exception: Exception) {
        binding.containerProgressBar.progressBar.visibility = View.GONE
        mContext.showDialogInformation(
            icon = InformationDialog.ICON_ERROR,
            title = "¡Oh, no! No se pudo crear la cuenta",
            text = "Error: ${exception.message}"
        )
    }

    private fun messageYouForgotData(data: String) {
        mContext.showDialogInformation(
            icon = InformationDialog.ICON_WARNING,
            title = "¡Vaya! No se puede crear la cuenta",
            text = "Parece que se te olvido ingresar $data." +
                    "\nRecuerda que este dato es obligatorio."
        )
    }
}