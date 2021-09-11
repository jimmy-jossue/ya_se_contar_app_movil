package com.janus.aprendiendonumeros.ui.fragment.auth.signup

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.data.remote.AuthDataSource
import com.janus.aprendiendonumeros.databinding.FragmentSignUpDataUserBinding
import com.janus.aprendiendonumeros.domain.auth.AuthImpl
import com.janus.aprendiendonumeros.presentation.AuthViewModel
import com.janus.aprendiendonumeros.presentation.AuthViewModelFactory
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.dialog.InformationDialog
import com.janus.aprendiendonumeros.ui.dialog.LoadingDialog
import com.janus.aprendiendonumeros.ui.listener.ControllablePager
import com.janus.aprendiendonumeros.ui.listener.ControllablePagerObserver
import com.janus.aprendiendonumeros.ui.listener.NotifyDrawableListener
import com.janus.aprendiendonumeros.ui.utilities.*

class SignUpDataUserFragment : BaseFragment(R.layout.fragment_sign_up_data_user),
    ControllablePagerObserver, NotifyDrawableListener {

    private lateinit var binding: FragmentSignUpDataUserBinding
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }
    private lateinit var controllablePager: ControllablePager
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthImpl(
                AuthDataSource()
            )
        )
    }

    override fun initUI(view: View) {
        binding = FragmentSignUpDataUserBinding.bind(view)
        addEvents()
        mContext.setDrawableTarget(this)

        if (User.staticInstance.gender == "female") {
            binding.ivProfileImage.setImageResource(R.drawable.ic_photo_profile_girl_deselected)
        }
    }

    private fun addEvents() {
        binding.btnAddImageProfile.setOnClickListener { mContext.showDialogProfileImage() }
        binding.btnForward.setOnClickListener { validateCredentials() }
        binding.btnBackward.setOnClickListener { controllablePager.backward() }
    }

    private fun validateCredentials() {
        val nickName: String = binding.etNickName.text.toString().trim()

        when {
            nickName.isEmpty() -> binding.etNickName.fieldIsEmpty(mContext, "un nombre de usuario")
            nickName.length.isLessThan(Constant.USER_NICKNAME_LENGTH) -> binding.etNickName.error =
                "El nombre de usuario debe tener mas de ${Constant.USER_NICKNAME_LENGTH} caracteres."
            else -> nickNameAlreadyExists(nickName)
        }
    }

    private fun nickNameAlreadyExists(nickName: String) {
        viewModel.fieldExistsInUser(
            User.NICK_NAME,
            nickName
        ).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> loadingDialog.startDialog("Cargando...")
                is Resource.Success -> resultSuccess(result.data)
                is Resource.Failure -> resultFailure()
            }
        })
    }

    private fun resultFailure() {
        loadingDialog.dismiss()
    }

    private fun resultSuccess(exist: Boolean) {
        loadingDialog.dismiss()
        if (exist.not()) {
            saveData()
        } else {
            messageNickNameAlreadyExists()
        }
    }

    private fun messageNickNameAlreadyExists() {
        mContext.showDialogInformation(
            icon = InformationDialog.ICON_WARNING,
            title = "¡Vaya! Este nombre de usuario ya esta en uso",
            text = "Elige un nombre de usuario diferente."
        )
    }

    private fun saveData() {
        val nickName: String = binding.etNickName.text.toString().trim()
        User.staticInstance.nickName = nickName
        Statics.profileImageBitmap = binding.ivProfileImage.drawable.toBitmap()
        controllablePager.forward()
    }

    override fun setControllablePager(controllablePager: ControllablePager) {
        this.controllablePager = controllablePager
    }

    override fun update(drawable: Drawable) {
        binding.ivProfileImage.setImageDrawable(drawable)
        val anim = UIAnimations(requireContext())
        anim.startAnimation(binding.ivProfileImage, R.anim.focus_attention)
        anim.startAnimation(binding.btnAddImageProfile, R.anim.focus_attention)
    }
}