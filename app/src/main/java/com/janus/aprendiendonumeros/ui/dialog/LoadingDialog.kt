package com.janus.aprendiendonumeros.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.DialogLoadingBinding

class LoadingDialog(private val mActivity: Activity) {

    private lateinit var dialog: AlertDialog
    private lateinit var binding: DialogLoadingBinding

    fun startDialog(loadingText: String) {
        val inflater: LayoutInflater = mActivity.layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_loading, null)
        binding = DialogLoadingBinding.bind(view)

        val builder = AlertDialog.Builder(mActivity)
        builder.setView(binding.root)
        builder.setCancelable(false)

        val wrapContent: Int = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog = builder.create()
        dialog.window?.setLayout(wrapContent, wrapContent)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.tvText.text = loadingText
        dialog.show()
    }

    fun setText(text: String) {
        binding.tvText.text = text
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }
}