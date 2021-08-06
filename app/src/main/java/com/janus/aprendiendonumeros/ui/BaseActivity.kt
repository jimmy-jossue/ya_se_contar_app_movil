package com.janus.aprendiendonumeros.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.janus.aprendiendonumeros.ui.dialog.ProfileImageDialog
import com.janus.aprendiendonumeros.ui.listener.NotifyDrawable

abstract class BaseActivity : AppCompatActivity(), ProfileImageDialog.Listener {

    private lateinit var drawableTarget: NotifyDrawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        initUI()
    }

    abstract fun getLayout(): Int

    abstract fun initUI()

    fun openDialogProfileImage() {
        val dialog = ProfileImageDialog()
        dialog.show(supportFragmentManager, null)
    }

    fun setDrawableTarget(drawableTarget: NotifyDrawable) {
        this.drawableTarget = drawableTarget
    }

    override fun onSelectionImage(dialog: ProfileImageDialog) {
        drawableTarget.update(dialog.getDrawable())
        dialog.dismiss()
    }
}