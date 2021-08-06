package com.janus.aprendiendonumeros.ui.fragment.login

import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.view.children
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.FragmentSignInBinding
import com.janus.aprendiendonumeros.ui.utilities.Constant
import com.janus.aprendiendonumeros.ui.utilities.UIAnimations
import com.janus.aprendiendonumeros.ui.utilities.goTo
import com.janus.aprendiendonumeros.ui.utilities.removeViews
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding
    private val listPassword: MutableMap<Int, ImageView> = mutableMapOf()
    private val anim: UIAnimations by lazy { UIAnimations(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)

        binding.btnSignUp.setOnClickListener { goTo(R.id.action_signIn_to_signUp) }
        binding.btnSignIn.setOnClickListener {

            if (binding.tgAgeMode.isChecked) {

            } else {
                var pass: String = ""
                listPassword.keys.forEach { pass += (it * 987654).toString() }
                loginChildren(pass)
            }


        }

        addEventImagesPassword()

        binding.tgAgeMode.setOnClickListener {
            val toggle: ToggleButton = it as ToggleButton
            changeMode(toggle.isChecked)
        }
    }

    private fun loginChildren(pass: String) {
        if (!pass.isEmpty()) {
            val encryptedPassword = encrypt(pass)
            Toast.makeText(context, "$encryptedPassword \n $pass", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No se eligo una contraseña", Toast.LENGTH_SHORT).show()
        }
    }

    private fun changeMode(isAdult: Boolean) {
        if (isAdult) {
            binding.containerModeAdult.visibility = View.VISIBLE
            binding.containerModeChild.visibility = View.GONE
        } else {
            binding.containerModeChild.visibility = View.VISIBLE
            binding.containerModeAdult.visibility = View.GONE
        }
    }

    private fun addEventImagesPassword() {
        val allImagesPassword = binding.containerImagesPassword.children.iterator()
        allImagesPassword.forEach { view ->
            view.setOnClickListener { selectImageForPassword((it as ImageView)) }
        }
    }

    private fun selectImageForPassword(view: ImageView) {
        val tag: String = view.tag.toString()
        if (tag == Constant.STATUS_DESELECTED) {
            if (listPassword.size < Constant.PASSWORD_LENGTH) {
                view.selectView(Constant.STATUS_SELECTED, R.drawable.ic_mark_selection, 0)
                val index: Int = binding.containerImagesPassword.indexOfChild(view) + 1
                listPassword[index] = view
                addImagesToContainer(listPassword.values.toList())
            } else
                listPassword.values.forEach { anim.startSimple(it, R.anim.attention) }
        } else {
            val padding = resources.getDimension(R.dimen.separation_extra_small).toInt()
            view.selectView(Constant.STATUS_DESELECTED, 0, padding)
            listPassword.remove(binding.containerImagesPassword.indexOfChild(view) + 1)
            addImagesToContainer(listPassword.values.toList())
        }
    }

    private fun ImageView.selectView(tag: String, resDrawable: Int, padding: Int) {
        this.tag = tag
        this.setBackgroundResource(resDrawable)
        this.setPadding(padding)
    }

    private fun addImagesToContainer(listImages: List<ImageView>) {
        if (binding.containerPreviewPassword.childCount > 0) {
            binding.containerPreviewPassword.removeViews()
        }

        listImages.forEach {
            val previewView: ImageView = ImageView(context).apply {
                val size: Int = resources.getDimension(R.dimen.height_button_small).toInt()
                this.layoutParams = ViewGroup.LayoutParams(size, size)
                this.adjustViewBounds = true
                this.setImageDrawable(it.drawable)
                this.alpha = 0.7F
                this.setPadding(3)
            }
            binding.containerPreviewPassword.addView(previewView)
        }
    }

    private fun encrypt(pass: String): String {
        val secretKey: SecretKeySpec = generateKey(Constant.ENCRYPTION_KEY)
        val cipher: Cipher = Cipher.getInstance(Constant.ENCRYPTION_AES)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val bytes = cipher.doFinal(pass.toByteArray())
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private fun generateKey(key: String): SecretKeySpec {
        val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        digest.update(key.toByteArray(), 0, key.toByteArray().size)
        val bytes = digest.digest()
        return SecretKeySpec(bytes, "AES")
    }
}