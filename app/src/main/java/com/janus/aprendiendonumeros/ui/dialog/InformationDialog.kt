package com.janus.aprendiendonumeros.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.DialogInformationBinding

class InformationDialog : DialogFragment(R.layout.dialog_information) {

    private lateinit var binding: DialogInformationBinding

    companion object {
        const val ARGUMENT_TITLE = "defaultTitle"
        const val ARGUMENT_TEXT = "defaultText"
        const val ARGUMENT_ICON = "icon"
        const val ICON_SUCCESS = "icon_success"
        const val ICON_WARNING = "icon_warning"
        const val ICON_ERROR = "icon_error"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogInformationBinding.bind(view)
        setUpUI()
        setUpData()
        setUpEvents()
    }

    private fun setUpUI() {
        isCancelable = false
        dialog?.let { dialog ->
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            hideSystemUI(dialog)
        }
    }

    private fun setUpData() {
        val args: Bundle? = arguments

        if (args != null) {
            val information: Information = when (args.getString(ARGUMENT_ICON)) {
                ICON_SUCCESS -> Information(
                    icon = R.drawable.ic_succes,
                    defaultTitle = "¡Todo listo!",
                    defaultText = "La tarea se realizó con éxito."
                )
                ICON_ERROR -> Information(
                    icon = R.drawable.ic_error,
                    defaultTitle = "¡Ocurrió un error!",
                    defaultText = "Lo sentimos.\nHubo  problemas para realizar la tarea."
                )
                else -> Information(
                    icon = R.drawable.ic_warning,
                    defaultTitle = "¡Aviso!",
                    defaultText = "La informmación para realizar la tareas es incorrecta."
                )
            }

            val title: String = args.getString(ARGUMENT_TITLE, information.defaultTitle)
            val text: String = args.getString(ARGUMENT_TEXT, information.defaultText)

            binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                information.icon))
            binding.tvTitle.text = title
            binding.tvText.text = text
        }
    }

    private fun setUpEvents() {
        binding.btnContinue.setOnClickListener { dismiss() }
    }

    private fun hideSystemUI(dialog: Dialog) {
        if (dialog.window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                dialog.window?.setDecorFitsSystemWindows(false)
            } else {
                if (activity != null) {
                    @Suppress("DEPRECATION")
                    dialog.window!!.decorView.systemUiVisibility =
                        requireActivity().window.decorView.systemUiVisibility
                }
            }
        }
    }

    private data class Information(val icon: Int, val defaultTitle: String, val defaultText: String)
}