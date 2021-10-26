package com.janus.aprendiendonumeros.ui.fragment.auth.signup

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.children
import androidx.fragment.app.viewModels
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.AESCrypt
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.data.remote.AuthDataSource
import com.janus.aprendiendonumeros.data.remote.UserDataSource
import com.janus.aprendiendonumeros.databinding.FragmentSignUpPasswordChildBinding
import com.janus.aprendiendonumeros.domain.auth.AuthImpl
import com.janus.aprendiendonumeros.presentation.AuthViewModel
import com.janus.aprendiendonumeros.presentation.AuthViewModelFactory
import com.janus.aprendiendonumeros.ui.animation.UIAnimations
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.dialog.InformationDialog
import com.janus.aprendiendonumeros.ui.dialog.LoadingDialog
import com.janus.aprendiendonumeros.ui.listener.ControllablePager
import com.janus.aprendiendonumeros.ui.listener.ControllablePagerObserver
import com.janus.aprendiendonumeros.ui.utilities.Constant
import com.janus.aprendiendonumeros.ui.utilities.removeViews

class SignUpPasswordChildFragment : BaseFragment(R.layout.fragment_sign_up_password_child),
    ControllablePagerObserver {

    private lateinit var binding: FragmentSignUpPasswordChildBinding
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }
    private lateinit var controllablePager: ControllablePager
    private val listPassword: MutableMap<Int, ImageView> = mutableMapOf()
    private val anim: UIAnimations by lazy { UIAnimations(requireContext()) }
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthImpl(
                AuthDataSource()
            )
        )
    }

    override fun initUI(view: View) {
        binding = FragmentSignUpPasswordChildBinding.bind(view)
        addEvents()
    }

    private fun addEvents() {
        binding.btnForward.setOnClickListener { validateCredentials() }
        binding.btnBackward.setOnClickListener { controllablePager.backward() }
        addEventImagesPassword()
    }

    private fun addEventImagesPassword() {
        val allImagesPassword =
            binding.containerImagesPassword.gridImagesPassword.children.iterator()
        allImagesPassword.forEach { view ->
            view.setOnClickListener { selectImageForPassword((it as ImageView)) }
        }
    }

    private fun validateCredentials() {
        var password = ""
        listPassword.keys.forEach { password += (it * 9876543).toString() }
        when {
            password.isEmpty() -> messagePasswordEmpty()
            else -> passwordAlreadyExists(password)
        }
    }

    private fun messagePasswordEmpty() {
        mContext.showDialogInformation(
            icon = InformationDialog.ICON_WARNING,
            title = "¡Vaya! Olvidaste elegir la contraseña",
            text = "Elegir una contraseña de inicio de sesion para el niño es obligatorio." +
                    "\n\nPor favor elige 3 imagenes para tu contraseña."
        )
    }

    private fun messagePasswordAlreadyExists() {
        mContext.showDialogInformation(
            icon = InformationDialog.ICON_WARNING,
            title = "¡Vaya! Esta contraseña ya esta en uso",
            text = "Elige una contraseña diferente."
        )
    }

    private fun passwordAlreadyExists(passwordChild: String) {
        val passwordEncrypt: String = AESCrypt.encrypt(passwordChild)

        viewModel.fieldExistsInUser(
            UserDataSource.PASSWORD_CHILD,
            passwordEncrypt
        ).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> loadingDialog.startDialog("Cargando...")
                is Resource.Success -> resultSuccess(result.data, passwordEncrypt)
                is Resource.Failure -> resultFailure()
            }
        })
    }

    private fun resultFailure() {
        loadingDialog.dismiss()
    }

    private fun resultSuccess(exist: Boolean, password: String) {
        loadingDialog.dismiss()
        if (exist.not()) {
            saveData(password)
        } else {
            messagePasswordAlreadyExists()
        }
    }

    private fun saveData(passwordChild: String) {
        User.staticInstance.passwordChild = passwordChild
        controllablePager.forward()
    }

    private fun selectImageForPassword(view: ImageView) {
        val tag: String = view.tag.toString()
        if (tag == Constant.STATUS_DESELECTED) {
            if (listPassword.size < Constant.USER_PASSWORD_CHILDREN_LENGTH) {
                view.selectView(Constant.STATUS_SELECTED, R.drawable.ic_mark_selection, 0)
                val index: Int =
                    binding.containerImagesPassword.gridImagesPassword.indexOfChild(view) + 1
                listPassword[index] = view
                addImagesToContainer(listPassword.values.toList())
            } else
                listPassword.values.forEach { anim.startAnimation(it, R.anim.attention) }
        } else {
            val padding = resources.getDimension(R.dimen.separation_extra_small).toInt()
            view.selectView(Constant.STATUS_DESELECTED, 0, padding)
            listPassword.remove(binding.containerImagesPassword.gridImagesPassword.indexOfChild(view) + 1)
            addImagesToContainer(listPassword.values.toList())
        }
    }

    private fun ImageView.selectView(tag: String, resDrawable: Int, padding: Int) {
        this.tag = tag
        this.setBackgroundResource(resDrawable)
        this.setPadding(padding, padding, padding, padding)
    }

    private fun addImagesToContainer(listImages: List<ImageView>) {
        binding.containerImagesPassword.previewPassword.removeViews()
        val padding = resources.getDimension(R.dimen.separation_extra_small).toInt()

        listImages.forEach {
            val previewView: ImageView = ImageView(context).apply {
                val size: Int = resources.getDimension(R.dimen.height_button_small).toInt()
                this.layoutParams = ViewGroup.LayoutParams(size, size)
                this.adjustViewBounds = true
                this.setImageDrawable(it.drawable)
                this.alpha = 0.7F
                this.setPadding(padding, padding, padding, padding)
            }
            binding.containerImagesPassword.previewPassword.addView(previewView)
        }
    }

    override fun setControllablePager(controllablePager: ControllablePager) {
        this.controllablePager = controllablePager
    }
}